/**
 * JavaScript principal para Yachay T'inkiy
 * Maneja la creación dinámica de módulos y lecciones
 */

// Variables globales para tracking de módulos y lecciones
let moduloCount = 0;
let leccionCounters = {};

/**
 * Agrega un nuevo módulo al formulario de creación de curso.
 * Crea la estructura HTML del módulo y lo inserta en el contenedor.
 */
function agregarModulo() {
    moduloCount++;
    leccionCounters[moduloCount] = 0;

    const modulosContainer = document.getElementById('modulosContainer');

    // Limpiar mensaje inicial si existe
    if (moduloCount === 1) {
        modulosContainer.innerHTML = '';
    }

    const moduloHTML = `
        <div class="module-card card mb-3" id="modulo-${moduloCount}" data-modulo-id="${moduloCount}">
            <div class="card-header bg-light d-flex justify-content-between align-items-center">
                <h6 class="mb-0">
                    <i class="fas fa-folder me-2"></i>
                    Módulo ${moduloCount}
                </h6>
                <div>
                    <button type="button" class="btn btn-sm btn-outline-primary" 
                            onclick="agregarLeccion(${moduloCount})">
                        <i class="fas fa-plus me-1"></i>Agregar Lección
                    </button>
                    <button type="button" class="btn btn-sm btn-outline-danger" 
                            onclick="eliminarModulo(${moduloCount})">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </div>
            <div class="card-body">
                <div class="row mb-3">
                    <div class="col-md-8">
                        <label class="form-label">Nombre del Módulo *</label>
                        <input type="text" 
                               class="form-control modulo-nombre" 
                               name="modulos[${moduloCount - 1}].nombre"
                               placeholder="Ej: Introducción"
                               required>
                    </div>
                    <div class="col-md-4">
                        <label class="form-label">Orden *</label>
                        <input type="number" 
                               class="form-control modulo-orden" 
                               name="modulos[${moduloCount - 1}].orden"
                               value="${moduloCount}"
                               min="1"
                               required>
                    </div>
                </div>
                
                <div id="lecciones-modulo-${moduloCount}" class="lecciones-container">
                    <div class="alert alert-secondary small">
                        <i class="fas fa-info-circle me-1"></i>
                        Agrega lecciones a este módulo
                    </div>
                </div>
            </div>
        </div>
    `;

    modulosContainer.insertAdjacentHTML('beforeend', moduloHTML);
}

/**
 * Agrega una nueva lección a un módulo específico.
 * 
 * @param {number} moduloId - ID del módulo al que se agregará la lección
 */
function agregarLeccion(moduloId) {
    const leccionCount = leccionCounters[moduloId];
    leccionCounters[moduloId]++;

    const leccionesContainer = document.getElementById(`lecciones-modulo-${moduloId}`);

    // Limpiar mensaje inicial si existe
    if (leccionCount === 0) {
        leccionesContainer.innerHTML = '';
    }

    const leccionHTML = `
        <div class="lesson-item border rounded p-3 mb-2" 
             id="leccion-${moduloId}-${leccionCount}"
             data-leccion-id="${leccionCount}">
            <div class="d-flex justify-content-between align-items-start mb-2">
                <h6 class="mb-0">
                    <i class="fas fa-play-circle text-primary me-2"></i>
                    Lección ${leccionCount + 1}
                </h6>
                <button type="button" class="btn btn-sm btn-outline-danger" 
                        onclick="eliminarLeccion(${moduloId}, ${leccionCount})">
                    <i class="fas fa-trash"></i>
                </button>
            </div>
            
            <div class="row">
                <div class="col-md-6 mb-2">
                    <label class="form-label small">Título *</label>
                    <input type="text" 
                           class="form-control form-control-sm leccion-titulo" 
                           name="modulos[${moduloId - 1}].lecciones[${leccionCount}].titulo"
                           placeholder="Ej: ¿Qué es HTML?"
                           maxlength="150"
                           required>
                </div>
                <div class="col-md-3 mb-2">
                    <label class="form-label small">Tipo *</label>
                    <select class="form-select form-select-sm leccion-tipo" 
                            name="modulos[${moduloId - 1}].lecciones[${leccionCount}].tipoContenido"
                            required>
                        <option value="">Seleccionar...</option>
                        <option value="VIDEO">Video</option>
                        <option value="TEXTO">Texto</option>
                        <option value="PDF">PDF</option>
                        <option value="QUIZ">Quiz</option>
                    </select>
                </div>
                <div class="col-md-3 mb-2">
                    <label class="form-label small">Orden *</label>
                    <input type="number" 
                           class="form-control form-control-sm leccion-orden" 
                           name="modulos[${moduloId - 1}].lecciones[${leccionCount}].orden"
                           value="${leccionCount + 1}"
                           min="1"
                           required>
                </div>
                <div class="col-12 mb-2">
                    <label class="form-label small">URL del Contenido</label>
                    <input type="url" 
                           class="form-control form-control-sm leccion-url" 
                           name="modulos[${moduloId - 1}].lecciones[${leccionCount}].urlContenido"
                           placeholder="https://youtube.com/watch?v=...">
                </div>
                <div class="col-12">
                    <label class="form-label small">Descripción</label>
                    <textarea class="form-control form-control-sm leccion-descripcion" 
                              name="modulos[${moduloId - 1}].lecciones[${leccionCount}].descripcion"
                              rows="2"
                              placeholder="Descripción breve de la lección..."
                              maxlength="1000"></textarea>
                </div>
            </div>
        </div>
    `;

    leccionesContainer.insertAdjacentHTML('beforeend', leccionHTML);
}

/**
 * Elimina un módulo completo del formulario.
 * 
 * @param {number} moduloId - ID del módulo a eliminar
 */
function eliminarModulo(moduloId) {
    if (confirm('¿Estás seguro de eliminar este módulo y todas sus lecciones?')) {
        const moduloElement = document.getElementById(`modulo-${moduloId}`);
        if (moduloElement) {
            moduloElement.remove();
        }

        // Si no quedan módulos, mostrar mensaje
        const modulosContainer = document.getElementById('modulosContainer');
        if (modulosContainer.children.length === 0) {
            modulosContainer.innerHTML = `
                <div class="alert alert-info">
                    <i class="fas fa-info-circle me-2"></i>
                    Haz clic en "Agregar Módulo" para comenzar a estructurar tu curso
                </div>
            `;
            moduloCount = 0;
        }
    }
}

/**
 * Elimina una lección específica de un módulo.
 * 
 * @param {number} moduloId - ID del módulo que contiene la lección
 * @param {number} leccionId - ID de la lección a eliminar
 */
function eliminarLeccion(moduloId, leccionId) {
    if (confirm('¿Estás seguro de eliminar esta lección?')) {
        const leccionElement = document.getElementById(`leccion-${moduloId}-${leccionId}`);
        if (leccionElement) {
            leccionElement.remove();
        }

        // Si no quedan lecciones en el módulo, mostrar mensaje
        const leccionesContainer = document.getElementById(`lecciones-modulo-${moduloId}`);
        if (leccionesContainer.children.length === 0) {
            leccionesContainer.innerHTML = `
                <div class="alert alert-secondary small">
                    <i class="fas fa-info-circle me-1"></i>
                    Agrega lecciones a este módulo
                </div>
            `;
            leccionCounters[moduloId] = 0;
        }
    }
}

/**
 * Reorganiza los índices de los campos del formulario para que sean consecutivos.
 * Spring Boot requiere índices [0], [1], [2]... sin huecos.
 * 
 * Esta función se llama antes de enviar el formulario para asegurar
 * que todos los índices sean válidos.
 */
function reorganizarIndices() {
    console.log('🔧 Reorganizando índices del formulario...');

    const modulosContainer = document.getElementById('modulosContainer');
    const modulos = modulosContainer.querySelectorAll('.module-card');

    console.log(`  📚 Módulos encontrados: ${modulos.length}`);

    // Reorganizar cada módulo
    modulos.forEach((modulo, moduloIndex) => {
        console.log(`  Procesando módulo ${moduloIndex}...`);

        // Actualizar campos del módulo
        const nombreInput = modulo.querySelector('.modulo-nombre');
        const ordenInput = modulo.querySelector('.modulo-orden');

        if (nombreInput)
            nombreInput.name = `modulos[${moduloIndex}].nombre`;
        if (ordenInput)
            ordenInput.name = `modulos[${moduloIndex}].orden`;

        // Reorganizar lecciones de este módulo
        const lecciones = modulo.querySelectorAll('.lesson-item');
        console.log(`    📄 Lecciones en módulo ${moduloIndex}: ${lecciones.length}`);

        lecciones.forEach((leccion, leccionIndex) => {
            // Actualizar todos los campos de la lección
            const tituloInput = leccion.querySelector('.leccion-titulo');
            const tipoSelect = leccion.querySelector('.leccion-tipo');
            const ordenInput = leccion.querySelector('.leccion-orden');
            const urlInput = leccion.querySelector('.leccion-url');
            const descripcionTextarea = leccion.querySelector('.leccion-descripcion');

            if (tituloInput) {
                tituloInput.name = `modulos[${moduloIndex}].lecciones[${leccionIndex}].titulo`;
            }
            if (tipoSelect) {
                tipoSelect.name = `modulos[${moduloIndex}].lecciones[${leccionIndex}].tipoContenido`;
            }
            if (ordenInput) {
                ordenInput.name = `modulos[${moduloIndex}].lecciones[${leccionIndex}].orden`;
            }
            if (urlInput) {
                urlInput.name = `modulos[${moduloIndex}].lecciones[${leccionIndex}].urlContenido`;
            }
            if (descripcionTextarea) {
                descripcionTextarea.name = `modulos[${moduloIndex}].lecciones[${leccionIndex}].descripcion`;
            }

            console.log(`      ✓ Lección ${leccionIndex}: ${tituloInput?.value || 'sin título'}`);
        });
    });

    console.log('✅ Índices reorganizados correctamente');
}

/**
 * Inicialización y validación del formulario.
 * Se ejecuta cuando el DOM está completamente cargado.
 */
document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('crearCursoForm');

    if (form) {
        form.addEventListener('submit', function (e) {
            console.log('\n📝 ===== VALIDANDO FORMULARIO =====');

            // 1. Reorganizar índices ANTES de validar
            reorganizarIndices();

            // 2. Validar que haya al menos un módulo
            const modulosContainer = document.getElementById('modulosContainer');
            const modulos = modulosContainer.querySelectorAll('.module-card');

            if (modulos.length === 0) {
                e.preventDefault();
                alert('⚠️ Debes agregar al menos un módulo al curso');
                console.log('❌ Validación fallida: Sin módulos');
                return false;
            }

            console.log(`✓ Módulos válidos: ${modulos.length}`);

            // 3. Validar que cada módulo tenga al menos una lección
            let todosLosModulosTienenLecciones = true;
            let modulosSinLecciones = [];

            modulos.forEach((modulo, index) => {
                const lecciones = modulo.querySelectorAll('.lesson-item');
                if (lecciones.length === 0) {
                    todosLosModulosTienenLecciones = false;
                    modulosSinLecciones.push(index + 1);
                }
            });

            if (!todosLosModulosTienenLecciones) {
                e.preventDefault();
                alert(`⚠️ Los siguientes módulos no tienen lecciones: ${modulosSinLecciones.join(', ')}\n\nCada módulo debe tener al menos una lección.`);
                console.log('❌ Validación fallida: Módulos sin lecciones:', modulosSinLecciones);
                return false;
            }

            console.log('✓ Todos los módulos tienen lecciones');

            // 4. Log de datos que se enviarán
            console.log('\n📤 Datos del formulario:');
            const formData = new FormData(form);
            for (let [key, value] of formData.entries()) {
                if (key.includes('modulos')) {
                    console.log(`  ${key}: ${value}`);
                }
            }

            // 5. Mostrar indicador de carga
            const submitBtn = form.querySelector('button[type="submit"]');
            submitBtn.disabled = true;
            submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Creando curso...';

            console.log('✅ Formulario válido. Enviando...\n');
        });
    }
});

/**
 * Auto-dismiss de alertas después de 5 segundos.
 */
document.addEventListener('DOMContentLoaded', function () {
    const alerts = document.querySelectorAll('.alert-dismissible');
    alerts.forEach(alert => {
        setTimeout(() => {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 5000);
    });
});

/**
 * Animación de entrada para tarjetas.
 * Usa Intersection Observer para animar elementos cuando entran en el viewport.
 */
document.addEventListener('DOMContentLoaded', function () {
    const cards = document.querySelectorAll('.course-card, .stats-card');

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('fade-in');
            }
        });
    }, {
        threshold: 0.1
    });

    cards.forEach(card => {
        observer.observe(card);
    });
});