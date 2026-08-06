

document.addEventListener('DOMContentLoaded', function() {
    // Función para reiniciar animaciones si es necesario
    function initCardAnimations() {
        const cards = document.querySelectorAll('.card-animate');
        
        // Remover y agregar la clase para reiniciar la animación
        cards.forEach((card, index) => {
            // Remover la clase de animación
            card.classList.remove('fade-in-up');
            
            // Forzar reflow
            void card.offsetWidth;
            
            // Agregar la clase de animación con delay escalonado
            setTimeout(() => {
                card.classList.add('fade-in-up');
            }, index * 200); // 200ms entre cada card
        });
    }

    // Inicializar animaciones al cargar la página
    initCardAnimations();

    // Opcional: Reiniciar animaciones al hacer resize (para responsive)
    window.addEventListener('resize', function() {
        // Solo reiniciar si el ancho de la ventana cambia significativamente
        clearTimeout(window.resizeTimer);
        window.resizeTimer = setTimeout(() => {
            initCardAnimations();
        }, 250);
    });

    // Efectos interactivos adicionales
    const cards = document.querySelectorAll('.card-animate');
    
    cards.forEach(card => {
        // Efecto al hacer click
        card.addEventListener('click', function() {
            this.style.transform = 'scale(0.98)';
            setTimeout(() => {
                this.style.transform = '';
            }, 150);
        });

        // Efecto al ganar foco (para accesibilidad)
        card.addEventListener('focus', function() {
            this.style.transform = 'translateY(-5px)';
        });

        card.addEventListener('blur', function() {
            this.style.transform = 'translateY(0)';
        });
    });

    // Intersection Observer para animaciones al hacer scroll (opcional)
    const cardObserver = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.style.animationPlayState = 'running';
            }
        });
    }, { threshold: 0.1 });

    // Observar todas las cards
    cards.forEach(card => {
        cardObserver.observe(card);
    });
});

// Función global para reiniciar animaciones manualmente
function restartCardAnimations() {
    const cards = document.querySelectorAll('.card-animate');
    
    cards.forEach((card, index) => {
        card.classList.remove('fade-in-up');
        void card.offsetWidth;
        
        setTimeout(() => {
            card.classList.add('fade-in-up');
        }, index * 200);
    });
    
}
