// Quando il DOM è completamente caricato...
document.addEventListener("DOMContentLoaded", () => {
    console.log("index.js caricato e pronto");

    //  1. RACCOLTA ELEMENTI
    const searchForm = document.querySelector('.search-form');
    const header2 = document.querySelector('.header .header-2');

    const menuBtn = document.querySelector('#menu-btn');
    const navbar = document.querySelector('.header .header-2 .navbar');

    const searchBtn = document.querySelector('#search-btn');
    const loginBtn = document.querySelector('#login-btn');
    const closeLoginBtn = document.querySelector('#close-login-btn');
    const userBtn = document.getElementById("user-btn");
    const userForm = document.getElementById("userForm");
    const closeBtn = document.getElementById("close-form");
    const closeToast = document.querySelector(".close-toast");

    const loginFormContainer = document.getElementById("login-form");
    const registerFormContainer = document.getElementById("register-form");
    const showRegisterLink = document.getElementById("show-register");
    const showLoginLink = document.getElementById("show-login");
    const openLoginFromCart = document.getElementById("open-login-from-cart");

    //  2. MENU MOBILE (Hamburger)
    if (menuBtn && navbar) {
        menuBtn.addEventListener('click', () => {
            // Alterna la classe 'show' per mostrare/nascondere il menu
            navbar.classList.toggle('show');
            // Alterna l'icona da "barre" a "X" (fa-times)
            menuBtn.classList.toggle('fa-times');
        });
    }

    // --- CHIUSURA CLICK ESTERNO ---
    document.addEventListener('click', (event) => {
        // Verifica se il menu è aperto e se l'elemento esiste
        if (navbar && navbar.classList.contains('show')) {
            // Definiamo chi ha ricevuto il click
            const isClickInsideMenu = navbar.contains(event.target);
            const isClickOnBtn = menuBtn.contains(event.target);

            // Se il click NON è nel menu E NON è sul bottone...
            if (!isClickInsideMenu && !isClickOnBtn) {
                // ... Chiudi il menu
                navbar.classList.remove('show');
                menuBtn.classList.remove('fa-times');
            }
        }
    });

    //  3. TOGGLE BARRA DI RICERCA
    if (searchBtn && searchForm) {
        searchBtn.onclick = () => {
            searchForm.classList.toggle('active');
        };
    }

    //  4. GESTIONE FORM LOGIN/REGISTRAZIONE
    if (userBtn && userForm) {
        userBtn.onclick = () => {
            userForm.classList.add("active");
            if(loginFormContainer) loginFormContainer.style.display = "block";
            if(registerFormContainer) registerFormContainer.style.display = "none";
        };
    }

    if (closeBtn && userForm) {
        closeBtn.onclick = () => userForm.classList.remove("active");
    }

    // Gestione bottoni extra (se presenti)
    if (loginBtn && userForm) loginBtn.onclick = () => userForm.classList.toggle('active');
    if (closeLoginBtn && userForm) closeLoginBtn.onclick = () => userForm.classList.remove('active');

    //  5. SWITCH LOGIN / REGISTRAZIONE
    if (showRegisterLink && showLoginLink && loginFormContainer && registerFormContainer) {
        showRegisterLink.addEventListener("click", (e) => {
            e.preventDefault();
            loginFormContainer.style.display = "none";
            registerFormContainer.style.display = "block";
        });

        showLoginLink.addEventListener("click", (e) => {
            e.preventDefault();
            registerFormContainer.style.display = "none";
            loginFormContainer.style.display = "block";
        });
    }

    //  6. LOGIN DAL CARRELLO
    if (openLoginFromCart && userForm) {
        openLoginFromCart.addEventListener("click", (e) => {
            e.preventDefault();
            userForm.classList.add("active");
            if(loginFormContainer) loginFormContainer.style.display = "block";
            if(registerFormContainer) registerFormContainer.style.display = "none";
        });
    }

    //  7. GESTIONE TOAST
    if (closeToast) {
        closeToast.addEventListener("click", () => {
            closeToast.parentElement.style.display = "none";
        });
    }
    setTimeout(() => {
        const toast = document.querySelector('.toast');
        if (toast) toast.style.display = 'none';
    }, 5500);

    //  8. EFFETTO SCROLL HEADER
    const handleScroll = () => {
        if (searchForm) searchForm.classList.remove('active');

        // CHIUDE IL MENU MOBILE SE SI SCORRE LA PAGINA
        if (navbar && menuBtn) {
            navbar.classList.remove('show');
            menuBtn.classList.remove('fa-times');
        }

        if (window.scrollY > 93) {
            header2?.classList.add('active');
        } else {
            header2?.classList.remove('active');
        }
    };
    window.onscroll = handleScroll;
    handleScroll();

    //  9. LOADER
    const loader = () => {
        document.querySelector('.loader-container')?.classList.add('active');
    };
    const fadeOut = () => setTimeout(loader, 4000);
    fadeOut();

    //  10. SWIPER (Carosello)
    const swiperContainer = document.querySelector('.featured-slider');
    if (typeof Swiper !== 'undefined' && swiperContainer) {
        const swiper = new Swiper('.featured-slider', {
            loop: true,
            spaceBetween: 20,
            autoplay: {
                delay: 4500,
                disableOnInteraction: false,
            },
            navigation: {
                nextEl: '.swiper-button-next',
                prevEl: '.swiper-button-prev',
            },
            breakpoints: {
                0: { slidesPerView: 1 },
                768: { slidesPerView: 2 },
                1024: { slidesPerView: 3 },
            },
        });
    }

    //  11. CHECKOUT INDIRIZZO
    const btnAggiungi = document.getElementById("btn-aggiungi-indirizzo");
    const btnAnnulla = document.getElementById("btn-annulla-indirizzo");
    const formAggiunta = document.getElementById("form-aggiungi-indirizzo");

    if (btnAggiungi && formAggiunta) {
        btnAggiungi.addEventListener("click", () => {
            formAggiunta.style.display = "block";
            btnAggiungi.style.display = "none";
        });
    }

    if (btnAnnulla && formAggiunta) {
        btnAnnulla.addEventListener("click", () => {
            formAggiunta.style.display = "none";
            btnAggiungi.style.display = "block";
        });
    }
});