// Quando il DOM è completamente caricato...
document.addEventListener("DOMContentLoaded", () => {
    // Mostra in console che lo script è stato caricato
    console.log("index.js caricato");

    //  RACCOLTA ELEMENTI PRINCIPALI DELLA PAGINA
    // Questi sono gli elementi che verranno manipolati con JS
    const searchForm = document.querySelector('.search-form'); // Barra di ricerca nella navbar
    const header2 = document.querySelector('.header .header-2'); // Seconda parte dell'header (usata per effetto scroll)
    const menuBtn = document.querySelector('#menu-btn'); // Bottone hamburger (menu mobile)
    const navbar = document.querySelector('.navbar'); // Menu di navigazione principale
    const searchBtn = document.querySelector('#search-btn'); // Eventuale bottone per attivare la ricerca (mobile)
    const loginBtn = document.querySelector('#login-btn'); // Bottone "Accedi" (può non essere sempre presente)
    const closeLoginBtn = document.querySelector('#close-login-btn'); // Bottone "Chiudi login"
    const userBtn = document.getElementById("user-btn"); // Icona utente (mostra login/registrazione)
    const userForm = document.getElementById("userForm"); // Popup del form login/registrazione
    const closeBtn = document.getElementById("close-form"); // Bottone X per chiudere il form login
    const closeToast = document.querySelector(".close-toast"); // Icona per chiudere il toast (messaggio popup)

    // Elementi login/registrazione e link di switch
    const loginFormContainer = document.getElementById("login-form"); // Form di login
    const registerFormContainer = document.getElementById("register-form"); // Form di registrazione
    const showRegisterLink = document.getElementById("show-register"); // Link "Registrati"
    const showLoginLink = document.getElementById("show-login"); // Link "Accedi"
    const openLoginFromCart = document.getElementById("open-login-from-cart"); // Bottone login dal carrello (se non loggato)

    //  MENU MOBILE: Mostra/nasconde la navbar se il menu hamburger viene cliccato
    if (menuBtn && navbar) {
        menuBtn.addEventListener('click', () => {
            navbar.classList.toggle('show');
        });
    }

    //  TOGGLE BARRA DI RICERCA: Mostra/nasconde la barra di ricerca
    if (searchBtn && searchForm) {
        searchBtn.onclick = () => {
            searchForm.classList.toggle('active');
        };
    }

    //  APERTURA/CHIUSURA FORM DI LOGIN: Gestisce apertura/chiusura del popup login/registrazione tramite bottoni
    if (loginBtn && userForm) {
        loginBtn.onclick = () => userForm.classList.toggle('active');
    }
    if (closeLoginBtn && userForm) {
        closeLoginBtn.onclick = () => userForm.classList.remove('active');
    }

    //  GESTIONE ICONA UTENTE: Mostra il popup login/registrazione al click sull'icona utente
    if (userBtn && userForm) {
        userBtn.onclick = () => {
            userForm.classList.add("active");
            loginFormContainer.style.display = "block"; // Mostra solo il form login
            registerFormContainer.style.display = "none"; // Nasconde il form registrazione
        };
    }
    if (closeBtn && userForm) {
        closeBtn.onclick = () => userForm.classList.remove("active");
    }

    // 6️⃣ SWITCH TRA LOGIN E REGISTRAZIONE: Mostra il form di registrazione o di login quando si cliccano i relativi link
    if (showRegisterLink && showLoginLink && loginFormContainer && registerFormContainer) {
        showRegisterLink.addEventListener("click", function (e) {
            e.preventDefault();
            loginFormContainer.style.display = "none"; // Nasconde login
            registerFormContainer.style.display = "block"; // Mostra registrazione
        });

        showLoginLink.addEventListener("click", function (e) {
            e.preventDefault();
            registerFormContainer.style.display = "none"; // Nasconde registrazione
            loginFormContainer.style.display = "block"; // Mostra login
        });
    }

    // 7️⃣ APERTURA LOGIN DAL CARRELLO: Se clicchi su "Effettua il login per acquistare", apre direttamente il login
    if (openLoginFromCart && userForm && loginFormContainer && registerFormContainer) {
        openLoginFromCart.addEventListener("click", function (e) {
            e.preventDefault();
            userForm.classList.add("active");
            loginFormContainer.style.display = "block"; // Mostra login
            registerFormContainer.style.display = "none"; // Nasconde registrazione
        });
    }

    // 8️⃣ GESTIONE TOAST: Chiude il messaggio di notifica al click sulla X
    if (closeToast) {
        closeToast.addEventListener("click", () => {
            closeToast.parentElement.style.display = "none";
        });
    }
    // Nasconde automaticamente il toast dopo 5.5 secondi
    setTimeout(() => {
        const toast = document.querySelector('.toast');
        if (toast) {
            toast.style.display = 'none';
        }
    }, 5500); // 5.5 secondi

    // 9️⃣ EFFETTO SCROLL HEADER: Fissa la barra in alto dopo uno scroll di 93px
    const handleScroll = () => {
        if (searchForm) searchForm.classList.remove('active'); // Chiude la search quando scrolli
        if (window.scrollY > 93) {
            header2?.classList.add('active');
        } else {
            header2?.classList.remove('active');
        }
    };
    window.onscroll = handleScroll; // Assegna la funzione all'evento scroll
    handleScroll(); // La richiama una volta al caricamento

    // 🔟 LOADER DI PAGINA: Dopo 4 secondi mostra un eventuale loader (animazione di caricamento)
    const loader = () => {
        document.querySelector('.loader-container')?.classList.add('active');
    };
    const fadeOut = () => setTimeout(loader, 4000);
    fadeOut();

    // 1️⃣1️⃣ INIZIALIZZAZIONE SWIPER (slider carosello libri): Solo se presente nella pagina e la libreria è caricata
    const swiperContainer = document.querySelector('.featured-slider');
    if (typeof Swiper !== 'undefined' && swiperContainer) {
        const swiper = new Swiper('.featured-slider', {
            loop: true, // Il carosello è ciclico
            spaceBetween: 20, // Spazio tra slide
            autoplay: {
                delay: 4500,
                disableOnInteraction: false, // Continua anche dopo un click
            },
            navigation: {
                nextEl: '.swiper-button-next', // Freccia avanti
                prevEl: '.swiper-button-prev', // Freccia indietro
            },
            slidesPerGroup: 1,
            breakpoints: {
                0: { slidesPerView: 1 },
                768: { slidesPerView: 2 },
                1024: {
                    slidesPerView: 3,
                    slidesPerGroup: 1,
                },
            },
        });

        // Ferma lo scorrimento automatico quando si passa il mouse sopra una slide
        const slides = document.querySelectorAll('.featured-slider .swiper-slide');
        slides.forEach(slide => {
            slide.addEventListener('mouseenter', () => swiper.autoplay.stop());
            slide.addEventListener('mouseleave', () => swiper.autoplay.start());
        });
    } else {
        console.log("🟡 Swiper non inizializzato: non richiesto in questa pagina");
    }

    // 1️⃣2️⃣ FORM AGGIUNTA INDIRIZZO NEL CHECKOUT: Mostra/nasconde il form aggiunta indirizzo
    const btnAggiungi = document.getElementById("btn-aggiungi-indirizzo"); // Bottone "Aggiungi indirizzo"
    const btnAnnulla = document.getElementById("btn-annulla-indirizzo"); // Bottone "Annulla"
    const formAggiunta = document.getElementById("form-aggiungi-indirizzo"); // Form aggiunta indirizzo

    // Al click su "Aggiungi", mostra il form e nasconde il bottone
    if (btnAggiungi && formAggiunta) {
        btnAggiungi.addEventListener("click", () => {
            formAggiunta.style.display = "block";
            btnAggiungi.style.display = "none";
        });
    }

    // Al click su "Annulla", nasconde il form e mostra il bottone "Aggiungi"
    if (btnAnnulla && formAggiunta) {
        btnAnnulla.addEventListener("click", () => {
            formAggiunta.style.display = "none";
            btnAggiungi.style.display = "block";
        });
    }
});