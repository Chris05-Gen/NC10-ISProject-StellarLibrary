document.addEventListener("DOMContentLoaded", () => {
    console.log("index.js caricato correttamente");

    /* =========================================
       1. GESTIONE MENU E RICERCA (HEADER)
       ========================================= */
    const searchForm = document.querySelector('.search-form');
    const header2 = document.querySelector('.header .header-2');
    const menuBtn = document.querySelector('#menu-btn');
    const navbar = document.querySelector('.header .header-2 .navbar');
    const searchBtn = document.querySelector('#search-btn');

    // Toggle Menu Mobile
    if (menuBtn && navbar) {
        menuBtn.addEventListener('click', () => {
            navbar.classList.toggle('show');
            menuBtn.classList.toggle('fa-times');
        });
    }

    // Chiudi menu cliccando fuori
    document.addEventListener('click', (event) => {
        if (navbar && navbar.classList.contains('show')) {
            if (!navbar.contains(event.target) && !menuBtn.contains(event.target)) {
                navbar.classList.remove('show');
                menuBtn.classList.remove('fa-times');
            }
        }
    });

    // Toggle barra di ricerca
    if (searchBtn && searchForm) {
        searchBtn.onclick = () => {
            searchForm.classList.toggle('active');
        };
    }

    /* =========================================
       2. GESTIONE POPUP LOGIN (Corretta per Navbar)
       ========================================= */
    // NOTA: Qui usiamo gli ID definiti nella nuova navbar.jsp
    const loginBtnTrigger = document.getElementById('login-btn-trigger'); // Bottone "Accedi" nella navbar
    const loginModal = document.getElementById('user-form-modal');       // Il contenitore scuro del popup
    const closeLoginBtn = document.getElementById('close-login-btn');    // La "X" per chiudere

    // Forms interni
    const loginFormBlock = document.getElementById('login-form');
    const registerFormBlock = document.getElementById('register-form');

    // Link per switchare
    const linkToRegister = document.getElementById('link-to-register'); // "Non hai account? Registrati"
    const linkToLogin = document.getElementById('link-to-login');       // "Hai account? Accedi"

    // Bottone speciale nel carrello "Login per acquistare"
    const openLoginFromCart = document.getElementById("open-login-from-cart");

    // FUNZIONE: APRI POPUP
    function openModal() {
        if(loginModal) {
            loginModal.style.display = "flex"; // Usa flex per centrare
            document.body.style.overflow = "hidden"; // Blocca lo scroll della pagina sotto
            // Reset: mostra sempre login all'apertura
            if(loginFormBlock) loginFormBlock.style.display = 'block';
            if(registerFormBlock) registerFormBlock.style.display = 'none';
        }
    }

    // FUNZIONE: CHIUDI POPUP
    function closeModal() {
        if(loginModal) {
            loginModal.style.display = "none";
            document.body.style.overflow = "auto"; // Riattiva scroll
        }
    }

    // Event Listeners
    if (loginBtnTrigger) {
        loginBtnTrigger.addEventListener('click', (e) => {
            e.preventDefault(); // Evita comportamenti strani dei link
            openModal();
        });
    }

    if (closeLoginBtn) {
        closeLoginBtn.addEventListener('click', closeModal);
    }

    // Chiudi cliccando fuori dal box bianco
    window.addEventListener('click', (event) => {
        if (event.target === loginModal) {
            closeModal();
        }
    });

    // Switch Login -> Registrati
    if (linkToRegister) {
        linkToRegister.addEventListener('click', (e) => {
            e.preventDefault();
            if(loginFormBlock) loginFormBlock.style.display = 'none';
            if(registerFormBlock) registerFormBlock.style.display = 'block';
        });
    }

    // Switch Registrati -> Login
    if (linkToLogin) {
        linkToLogin.addEventListener('click', (e) => {
            e.preventDefault();
            if(registerFormBlock) registerFormBlock.style.display = 'none';
            if(loginFormBlock) loginFormBlock.style.display = 'block';
        });
    }

    // Apertura dal carrello
    if (openLoginFromCart) {
        openLoginFromCart.addEventListener("click", (e) => {
            e.preventDefault();
            openModal();
        });
    }

    /* =========================================
       3. GESTIONE TOAST E UTILITY
       ========================================= */

    // Chiudi Toast manuale
    const closeToasts = document.querySelectorAll(".close-toast");
    closeToasts.forEach(btn => {
        btn.addEventListener("click", function() {
            this.parentElement.style.display = "none";
        });
    });

    // Auto-hide Toast dopo 5.5 secondi
    setTimeout(() => {
        const toasts = document.querySelectorAll('.toast');
        toasts.forEach(t => t.style.display = 'none');
    }, 5500);

    // Sticky Header Scroll
    window.onscroll = () => {
        if (searchForm) searchForm.classList.remove('active');
        if (navbar && menuBtn) {
            navbar.classList.remove('show');
            menuBtn.classList.remove('fa-times');
        }
        if (window.scrollY > 80) {
            header2?.classList.add('active');
        } else {
            header2?.classList.remove('active');
        }
    };

    // Loader Fade Out
    const loaderContainer = document.querySelector('.loader-container');
    if(loaderContainer) {
        setTimeout(() => {
            loaderContainer.classList.add('active'); // O remove, dipende dal tuo CSS
        }, 4000);
    }

    // Swiper (Slider)
    if (typeof Swiper !== 'undefined') {
        new Swiper('.featured-slider', {
            loop: true,
            spaceBetween: 20,
            autoplay: { delay: 4500, disableOnInteraction: false },
            navigation: { nextEl: '.swiper-button-next', prevEl: '.swiper-button-prev' },
            breakpoints: {
                0: { slidesPerView: 1 },
                768: { slidesPerView: 2 },
                1024: { slidesPerView: 3 },
            },
        });
    }
});