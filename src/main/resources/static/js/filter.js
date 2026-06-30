/**
 * Filtra cursos por estado (todos, progreso, completados)
 */
function filtrarCursos(filtro) {
    console.log('🔍 Filtrando por:', filtro);

    // Obtener todas las cards de cursos
    const cards = document.querySelectorAll('.curso-card-wrapper');
    let contadorVisibles = 0;

    // Iterar y mostrar/ocultar según filtro
    cards.forEach(card => {
        const estado = card.getAttribute('data-estado');
        let mostrar = false;

        switch (filtro) {
            case 'todos':
                mostrar = true;
                break;
            case 'progreso':
                mostrar = (estado === 'progreso');
                break;
            case 'completados':
                mostrar = (estado === 'completado');
                break;
        }

        if (mostrar) {
            card.style.display = '';
            contadorVisibles++;
        } else {
            card.style.display = 'none';
        }
    });

    // Actualizar botones activos
    document.querySelectorAll('.btn-group button').forEach(btn => {
        btn.classList.remove('active');
    });
    document.getElementById('filtro-' + filtro).classList.add('active');

    // Mostrar mensaje si no hay cursos
    mostrarMensajeSinResultados(contadorVisibles, filtro);

    console.log(`✅ ${contadorVisibles} cursos mostrados`);
}

/**
 * Muestra mensaje cuando no hay cursos que coincidan con el filtro
 */
function mostrarMensajeSinResultados(cantidad, filtro) {
    // Remover mensaje existente si hay
    const mensajeExistente = document.getElementById('mensaje-sin-resultados');
    if (mensajeExistente) {
        mensajeExistente.remove();
    }

    // Si no hay cursos visibles, mostrar mensaje
    if (cantidad === 0) {
        const grid = document.getElementById('cursos-grid');
        const mensaje = document.createElement('div');
        mensaje.id = 'mensaje-sin-resultados';
        mensaje.className = 'col-12 text-center py-5';

        let textoMensaje = '';
        if (filtro === 'progreso') {
            textoMensaje = 'No tienes cursos en progreso';
        } else if (filtro === 'completados') {
            textoMensaje = 'Aún no has completado ningún curso';
        }

        mensaje.innerHTML = `
            <i class="fas fa-inbox text-muted" style="font-size: 4rem;"></i>
            <h5 class="mt-3 text-muted">${textoMensaje}</h5>
        `;

        grid.appendChild(mensaje);
    }
}
// Verificar que el script se carga
console.log('✅ filter.js cargado correctamente');

// Verificar que las funciones existen
document.addEventListener('DOMContentLoaded', function () {
    console.log('🔍 DOM cargado');

    // Verificar cards
    const cards = document.querySelectorAll('.curso-card-wrapper');
    console.log(`📦 Cards encontradas: ${cards.length}`);

    // Verificar data-estado
    cards.forEach((card, index) => {
        console.log(`  Card ${index}: data-estado = "${card.getAttribute('data-estado')}"`);
    });

    // Verificar botones
    const botones = document.querySelectorAll('.btn-group button');
    console.log(`🔘 Botones encontrados: ${botones.length}`);
});