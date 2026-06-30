// Cargar todas las lecciones en un array para navegación
document.addEventListener('DOMContentLoaded', function () {
    console.log('🎬 Inicializando player de curso...');

    // Recopilar todas las lecciones en orden
    document.querySelectorAll('.leccion-item').forEach(item => {
        todasLasLecciones.push({
            id: parseInt(item.dataset.leccionId),
            titulo: item.dataset.titulo,
            tipo: item.dataset.tipo,
            url: item.dataset.url,
            descripcion: item.dataset.descripcion,
            element: item
        });
    });

    console.log(`  📚 Total de lecciones: ${todasLasLecciones.length}`);

    // Cargar la lección inicial
    if (leccionActualId) {
        const leccionInicial = todasLasLecciones.find(l => l.id === leccionActualId);
        if (leccionInicial) {
            cargarContenidoLeccion(leccionInicial);
        }
    }

    // Configurar botones de navegación
    document.getElementById('btn-anterior').addEventListener('click', navegarAnterior);
    document.getElementById('btn-siguiente').addEventListener('click', navegarSiguiente);
});

/**
 * Carga una lección cuando se hace click en el sidebar.
 */
function cargarLeccion(element) {
    const leccion = {
        id: parseInt(element.dataset.leccionId),
        titulo: element.dataset.titulo,
        tipo: element.dataset.tipo,
        url: element.dataset.url,
        descripcion: element.dataset.descripcion,
        element: element
    };

    cargarContenidoLeccion(leccion);
}

/**
 * Carga el contenido de una lección en el área principal.
 */
function cargarContenidoLeccion(leccion) {
    console.log(`📖 Cargando lección: ${leccion.titulo} (${leccion.tipo})`);

    leccionActualId = leccion.id;

    // Actualizar clases active en sidebar
    document.querySelectorAll('.leccion-item').forEach(item => {
        item.classList.remove('active');
    });
    leccion.element.classList.add('active');

    // Actualizar URL sin recargar página
    const newUrl = `/cursos/${cursoId}/aprender?leccionId=${leccion.id}`;
    window.history.pushState({leccionId: leccion.id}, '', newUrl);

    // Renderizar contenido según tipo
    const contenedor = document.getElementById('contenido-leccion');

    switch (leccion.tipo) {
        case 'VIDEO':
            contenedor.innerHTML = renderizarVideo(leccion);
            break;
        case 'PDF':
            contenedor.innerHTML = renderizarPDF(leccion);
            break;
        case 'TEXTO':
            contenedor.innerHTML = renderizarTexto(leccion);
            break;
        case 'QUIZ':
            contenedor.innerHTML = renderizarQuiz(leccion);
            break;
        default:
            contenedor.innerHTML = renderizarError(leccion);
    }

    // Actualizar botones de navegación
    actualizarBotonesNavegacion();
}

/**
 * Renderiza un video de YouTube o Vimeo.
 */
function renderizarVideo(leccion) {
    let embedUrl = leccion.url;

    // Convertir URL de YouTube a embed
    if (embedUrl && embedUrl.includes('youtube.com/watch')) {
        const videoId = new URL(embedUrl).searchParams.get('v');
        embedUrl = `https://www.youtube.com/embed/${videoId}`;
    } else if (embedUrl && embedUrl.includes('youtu.be/')) {
        const videoId = embedUrl.split('youtu.be/')[1].split('?')[0];
        embedUrl = `https://www.youtube.com/embed/${videoId}`;
    }

    return `
                        <h3 class="mb-3">${leccion.titulo}</h3>
                        <div class="video-container" style="padding-bottom: 56.25%; height: 0; position: relative; overflow: hidden;">
                            <iframe src="${embedUrl}" 
                                    style="position: absolute; top: 0; left: 0; width: 100%; height: 100%;" 
                                    allowfullscreen
                                    allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture">
                            </iframe>
                        </div>
                        ${leccion.descripcion ? `<p class="text-muted">${leccion.descripcion}</p>` : ''}
                        <div class="alert alert-info mt-3">
                            <i class="fas fa-info-circle me-2"></i>
                            <strong>Nota:</strong> Marca esta lección como completada cuando termines de ver el video.
                        </div>
                        <div class="text-center mt-4">
                            <button class="btn btn-success btn-lg" onclick="marcarLeccionCompletada(${leccion.id})">
                                <i class="fas fa-check-circle me-2"></i>Marcar como Completada
                            </button>
                        </div>
                    `;
}

/**
 * Renderiza un PDF.
 */
function renderizarPDF(leccion) {
    return `
                        <h3 class="mb-3">${leccion.titulo}</h3>
                        ${leccion.descripcion ? `<p class="text-muted mb-3">${leccion.descripcion}</p>` : ''}
                        <embed src="${leccion.url}" 
                               type="application/pdf" 
                               class="pdf-viewer"       
                               style="width: 100%; min-height: 800px; border: 1px solid #dee2e6; border-radius: 8px;">
                        <div class="alert alert-warning mt-3">
                            <i class="fas fa-info-circle me-2"></i>
                            <strong>Nota:</strong> Si no puedes ver el PDF, 
                            <a href="${leccion.url}" target="_blank" class="alert-link">ábrelo en una nueva pestaña</a>.
                        </div>
                        <div class="text-center mt-4">
                            <button class="btn btn-success btn-lg" onclick="marcarLeccionCompletada(${leccion.id})">
                                <i class="fas fa-check-circle me-2"></i>Marcar como Completada
                            </button>
                        </div>
                    `;
}

/**
 * Renderiza contenido de texto.
 */
function renderizarTexto(leccion) {
    return `
                        <h3 class="mb-4">${leccion.titulo}</h3>
                        <div class="leccion-texto">
                            ${leccion.descripcion ? `<p>${leccion.descripcion}</p>` : '<p class="text-muted">Sin contenido disponible.</p>'}
                        </div>
                        <hr class="my-4">
                        <!-- Botón para marcar como completada -->
                        <div class="text-center">
                            <button class="btn btn-success btn-lg" onclick="marcarLeccionCompletada(${leccion.id})">
                                <i class="fas fa-check-circle me-2"></i>Marcar como Completada
                            </button>
                        </div>
                    `;
}

/**
 * Renderiza un quiz (enlace externo).
 */
function renderizarQuiz(leccion) {
    return `
                        <h3 class="mb-3">${leccion.titulo}</h3>
                        ${leccion.descripcion ? `<p class="text-muted mb-4">${leccion.descripcion}</p>` : ''}
                        <div class="text-center py-5">
                            <i class="fas fa-question-circle text-primary" style="font-size: 4rem;"></i>
                            <h4 class="mt-3 mb-4">Quiz Interactivo</h4>
                            <a href="${leccion.url}" 
                               target="_blank" 
                               class="btn btn-primary btn-lg">
                                <i class="fas fa-external-link-alt me-2"></i>
                                Abrir Quiz
                            </a>
                            <!-- Botón para marcar como completada -->
                            <div class="mt-4">
                                <button class="btn btn-success btn-lg" onclick="marcarLeccionCompletada(${leccion.id})">
                                    <i class="fas fa-check-circle me-2"></i>Marcar como Completada
                                </button>
                            </div>    
                        </div>
                        <div class="alert alert-warning mt-4">
                            <i class="fas fa-exclamation-triangle me-2"></i>
                            El quiz se abrirá en una nueva pestaña. Marca como completada cuando termines.
                        </div>
                    `;
}

/**
 * Renderiza un error si no hay URL.
 */
function renderizarError(leccion) {
    return `
                        <h3 class="mb-3">${leccion.titulo}</h3>
                        <div class="alert alert-danger">
                            <i class="fas fa-exclamation-circle me-2"></i>
                            No hay contenido disponible para esta lección.
                        </div>
                    `;
}

/**
 * Navega a la lección anterior.
 */
function navegarAnterior() {
    const indiceActual = todasLasLecciones.findIndex(l => l.id === leccionActualId);
    if (indiceActual > 0) {
        cargarContenidoLeccion(todasLasLecciones[indiceActual - 1]);
    }
}

/**
 * Navega a la siguiente lección.
 */
function navegarSiguiente() {
    const indiceActual = todasLasLecciones.findIndex(l => l.id === leccionActualId);
    if (indiceActual < todasLasLecciones.length - 1) {
        cargarContenidoLeccion(todasLasLecciones[indiceActual + 1]);
    }
}

/**
 * Actualiza el estado de los botones de navegación.
 */
function actualizarBotonesNavegacion() {
    const indiceActual = todasLasLecciones.findIndex(l => l.id === leccionActualId);

    const btnAnterior = document.getElementById('btn-anterior');
    const btnSiguiente = document.getElementById('btn-siguiente');

    // Botón anterior
    if (indiceActual === 0) {
        btnAnterior.disabled = true;
    } else {
        btnAnterior.disabled = false;
    }

    // Botón siguiente
    if (indiceActual === todasLasLecciones.length - 1) {
        btnSiguiente.disabled = true;
        btnSiguiente.innerHTML = '<i class="fas fa-trophy me-2"></i>Curso Completado';
    } else {
        btnSiguiente.disabled = false;
        btnSiguiente.innerHTML = 'Siguiente Lección<i class="fas fa-chevron-right ms-2"></i>';
    }
}
/**
 * Marca una lección como completada.
 * Envía petición POST al servidor y actualiza la UI en tiempo real.
 */
async function marcarLeccionCompletada(leccionId) {
    console.log(`✅ Marcando lección ${leccionId} como completada...`);

    const btn = event.target.closest('button');
    const textoOriginal = btn.innerHTML;

    // Mostrar loading
    btn.disabled = true;
    btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Guardando...';

    try {
        // Obtener inscripción ID desde el progreso
        //const inscripcionId = obtenerInscripcionId();

        // Enviar petición POST
        const response = await fetch(`/cursos/inscripciones/${inscripcionId}/lecciones/${leccionId}/completar`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [csrfHeader]: csrfToken
            }
        });

        if (!response.ok) {
            throw new Error('Error al marcar lección');
        }

        // Obtener progreso actualizado
        const progresoActualizado = await response.json();

        console.log('✅ Lección marcada exitosamente');
        console.log(`  Progreso: ${progresoActualizado.porcentajeProgreso}%`);

        // Actualizar UI
        actualizarProgresoUI(progresoActualizado);

        // Cambiar botón a "Completada"
        btn.innerHTML = '<i class="fas fa-check-circle me-2"></i>¡Completada!';
        btn.classList.remove('btn-success');
        btn.classList.add('btn-secondary');

        // Mensaje de éxito
        mostrarMensaje('¡Lección completada!', 'success');

    } catch (error) {
        console.error('Error:', error);
        btn.disabled = false;
        btn.innerHTML = textoOriginal;
        mostrarMensaje('Error al marcar lección. Intenta nuevamente.', 'danger');
    }
}

/**
 * Actualiza la UI con el progreso actualizado.
 */
function actualizarProgresoUI(progreso) {
    // 1. Actualizar barra de progreso general
    const barraProgreso = document.querySelector('.progress-bar');
    barraProgreso.style.width = progreso.porcentajeProgreso + '%';

    // 2. Actualizar contador de lecciones
    const contadores = document.querySelectorAll('small.text-muted');
    if (contadores[0]) {
        contadores[0].innerHTML = `
                            <i class="fas fa-check-circle text-success me-1"></i>
                            ${progreso.leccionesCompletadas} / ${progreso.totalLecciones} lecciones
                        `;
    }
    if (contadores[1]) {
        contadores[1].innerHTML = `
                            <i class="fas fa-chart-line me-1"></i>
                            ${Math.round(progreso.porcentajeProgreso)}% completado
                        `;
    }

    // 3. Actualizar checkmarks en sidebar
    progreso.modulos.forEach(modulo => {
        modulo.lecciones.forEach(leccion => {
            const leccionItem = document.querySelector(`[data-leccion-id="${leccion.leccionId}"]`);
            if (leccionItem) {
                // Agregar clase completada
                if (leccion.completada) {
                    leccionItem.classList.add('completada');

                    // Agregar checkmark si no existe
                    if (!leccionItem.querySelector('.fa-check-circle')) {
                        const check = document.createElement('i');
                        check.className = 'fas fa-check-circle float-end text-success';
                        leccionItem.appendChild(check);
                    }
                }
            }
        });
    });
}

/**
 * Obtiene el ID de inscripción desde el DOM.
 
function obtenerInscripcionId() {
    // Ya tenemos inscripcionId como variable global desde Thymeleaf
    return inscripcionId;
}
*/
/**
 * Muestra un mensaje toast al usuario.
 */
function mostrarMensaje(texto, tipo) {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${tipo} alert-dismissible fade show position-fixed top-0 start-50 translate-middle-x mt-3`;
    alertDiv.style.zIndex = '9999';
    alertDiv.innerHTML = `
                        ${texto}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    `;

    document.body.appendChild(alertDiv);

    // Auto-cerrar después de 3 segundos
    setTimeout(() => {
        alertDiv.remove();
    }, 3000);
}