package com.utp.yachaytinkiy.application.service;

import com.utp.yachaytinkiy.domain.model.*;
import com.utp.yachaytinkiy.domain.repository.*;
import com.utp.yachaytinkiy.application.dto.CertificadoDTO;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import java.util.stream.Collectors;

/**
 * Servicio para gestión de certificados con PDFMonkey.
 */
@Service
public class CertificadoService {

    private final CertificadoRepository certificadoRepository;
    private final AlumnoRepository alumnoRepository;
    private final CursoRepository cursoRepository;

    @Value("${pdfmonkey.api.key}")
    private String pdfMonkeyApiKey;

    @Value("${pdfmonkey.template.id}")
    private String pdfMonkeyTemplateId;

    @Value("${pdfmonkey.api.url}")
    private String pdfMonkeyApiUrl;

    public CertificadoService(CertificadoRepository certificadoRepository,
            AlumnoRepository alumnoRepository,
            CursoRepository cursoRepository) {
        this.certificadoRepository = certificadoRepository;
        this.alumnoRepository = alumnoRepository;
        this.cursoRepository = cursoRepository;
    }

    /**
     * Genera un certificado para un alumno al completar un curso.
     *
     * Este método: 1. Valida que no exista certificado previo 2. Genera código
     * de verificación único 3. Llama a PDFMonkey API para generar PDF 4. Guarda
     * el certificado en BD con la URL del PDF
     */
    @Transactional
    public CertificadoDTO generarCertificado(Long alumnoId, Long cursoId) {

        System.out.println("\n🎓 ===== GENERANDO CERTIFICADO =====");
        System.out.println("  Alumno ID: " + alumnoId);
        System.out.println("  Curso ID: " + cursoId);

        // 1. Validar que alumno existe
        Alumno alumno = alumnoRepository.findById(alumnoId)
                .orElseThrow(() -> new IllegalArgumentException(
                "Alumno no encontrado con ID: " + alumnoId
        ));

        // 2. Validar que curso existe
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new IllegalArgumentException(
                "Curso no encontrado con ID: " + cursoId
        ));

        // 3. Verificar que NO exista certificado previo
        if (certificadoRepository.existsByAlumnoIdAndCursoId(alumnoId, cursoId)) {
            System.out.println("  ⚠️ Certificado ya existe, retornando existente");
            Certificado existente = certificadoRepository
                    .findByAlumnoIdAndCursoId(alumnoId, cursoId)
                    .orElseThrow();
            return toDTO(existente);
        }

        // 4. Generar código de verificación único
        String codigoVerificacion = generarCodigoVerificacion();
        System.out.println("  Código verificación: " + codigoVerificacion);

        // 5. Llamar a PDFMonkey API
        String urlPdf;
        try {
            urlPdf = generarPdfConPDFMonkey(alumno, curso, codigoVerificacion);
            System.out.println("  ✅ PDF generado: " + urlPdf);
        } catch (Exception e) {
            System.err.println("  🔴 Error al generar PDF: " + e.getMessage());
            throw new RuntimeException("Error al generar certificado con PDFMonkey", e);
        }

        // 6. Crear y guardar certificado
        Certificado certificado = new Certificado(alumno, curso, codigoVerificacion);
        certificado.setUrlPdf(urlPdf);

        Certificado certificadoGuardado = certificadoRepository.save(certificado);

        System.out.println("  ✅ Certificado guardado con ID: " + certificadoGuardado.getId());
        System.out.println("🎓 ===================================\n");

        return toDTO(certificadoGuardado);
    }
    /**
     * Llama a la API de PDFMonkey para generar el PDF del certificado. NOTA:
     * PDFMonkey genera PDFs de forma asíncrona, por lo que debemos esperar a
     * que el documento esté listo antes de obtener la URL.
     */
    private String generarPdfConPDFMonkey(Alumno alumno, Curso curso,
            String codigoVerificacion) throws Exception {

        // Formatear fecha actual
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy");
        String fechaEmision = LocalDateTime.now().format(formatter);

        // Construir payload JSON para PDFMonkey
        JSONObject payload = new JSONObject();
        payload.put("document", new JSONObject()
                .put("document_template_id", pdfMonkeyTemplateId)
                .put("status", "pending")
                .put("payload", new JSONObject()
                        .put("nombreAlumno", alumno.getNombreCompleto())
                        .put("tituloCurso", curso.getTitulo())
                        .put("nombreDocente", curso.getDocente().getNombreCompleto())
                        .put("fechaEmision", fechaEmision)
                        .put("codigoVerificacion", codigoVerificacion)
                        .put("duracionCurso", curso.getDuracion() + " horas")
                )
        );

        System.out.println("  📤 Enviando a PDFMonkey:");
        System.out.println("     Template ID: " + pdfMonkeyTemplateId);
        System.out.println("     Alumno: " + alumno.getNombreCompleto());
        System.out.println("     Curso: " + curso.getTitulo());

        // Crear cliente HTTP
        OkHttpClient client = new OkHttpClient();

        // Construir request
        RequestBody body = RequestBody.create(
                payload.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(pdfMonkeyApiUrl)
                .addHeader("Authorization", "Bearer " + pdfMonkeyApiKey)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        // Ejecutar request para CREAR el documento
        String documentId;
        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "Sin detalles";
                System.err.println("  🔴 Error de PDFMonkey: " + response.code());
                System.err.println("     Body: " + errorBody);
                throw new RuntimeException(
                        "Error de PDFMonkey API: " + response.code() + " - " + errorBody
                );
            }

            // Parsear respuesta
            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);

            // Extraer ID del documento
            JSONObject document = jsonResponse.getJSONObject("document");
            documentId = document.getString("id");

            System.out.println("  ✅ Documento creado con ID: " + documentId);
            System.out.println("  ⏳ Esperando a que PDFMonkey genere el PDF...");
        }

        // ✅ ESPERAR A QUE EL PDF ESTÉ LISTO
        String downloadUrl = esperarPdfListo(client, documentId);

        System.out.println("  ✅ PDF listo para descargar");

        return downloadUrl;
    }

    /**
     * Espera a que PDFMonkey termine de generar el PDF y retorna la URL. Hace
     * polling cada 2 segundos hasta que el documento esté listo.
     */
    private String esperarPdfListo(OkHttpClient client, String documentId) throws Exception {

        int intentos = 0;
        int maxIntentos = 30; // Máximo 60 segundos (30 intentos × 2 seg)

        while (intentos < maxIntentos) {

            // Construir request para obtener el estado del documento
            Request request = new Request.Builder()
                    .url(pdfMonkeyApiUrl + "/" + documentId)
                    .addHeader("Authorization", "Bearer " + pdfMonkeyApiKey)
                    .get()
                    .build();

            try (Response response = client.newCall(request).execute()) {

                if (!response.isSuccessful()) {
                    throw new RuntimeException("Error al verificar estado del documento");
                }

                String responseBody = response.body().string();
                JSONObject jsonResponse = new JSONObject(responseBody);
                JSONObject document = jsonResponse.getJSONObject("document");

                String status = document.getString("status");

                System.out.println("     Estado: " + status + " (intento " + (intentos + 1) + "/" + maxIntentos + ")");

                // Si el documento está listo, retornar URL
                if ("success".equals(status)) {

                    // Verificar que download_url no sea null
                    if (document.isNull("download_url")) {
                        throw new RuntimeException("PDFMonkey no retornó download_url");
                    }

                    return document.getString("download_url");
                }

                // Si hay error, lanzar excepción
                if ("failure".equals(status)) {
                    String error = document.optString("error", "Error desconocido");
                    throw new RuntimeException("PDFMonkey falló al generar PDF: " + error);
                }

                // Si aún está procesando, esperar 2 segundos
                Thread.sleep(2000);
                intentos++;
            }
        }

        // Si llegamos aquí, se agotó el tiempo de espera
        throw new RuntimeException(
                "Timeout: PDFMonkey no terminó de generar el PDF en 60 segundos"
        );
    }
    /**
     * Genera un código de verificación único para el certificado.
     */
    private String generarCodigoVerificacion() {
        // Formato: CERT-XXXXXX (6 caracteres aleatorios)
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
        return "CERT-" + uuid;
    }

    /**
     * Obtiene todos los certificados de un alumno.
     */
    @Transactional(readOnly = true)
    public List<CertificadoDTO> listarCertificadosPorAlumno(Long alumnoId) {
        return certificadoRepository.findByAlumnoId(alumnoId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un certificado por su código de verificación.
     */
    @Transactional(readOnly = true)
    public CertificadoDTO obtenerPorCodigoVerificacion(String codigo) {
        Certificado certificado = certificadoRepository.findByCodigoVerificacion(codigo)
                .orElseThrow(() -> new IllegalArgumentException(
                "Certificado no encontrado con código: " + codigo
        ));
        return toDTO(certificado);
    }

    /**
     * Convierte entidad Certificado a DTO.
     */
    private CertificadoDTO toDTO(Certificado certificado) {
        return new CertificadoDTO(
                certificado.getId(),
                certificado.getAlumno().getNombreCompleto(),
                certificado.getAlumno().getEmail(),
                certificado.getCurso().getTitulo(),
                certificado.getCurso().getDocente().getNombreCompleto(),
                certificado.getCodigoVerificacion(),
                certificado.getFechaEmision(),
                certificado.getUrlPdf()
        );
    }
}
