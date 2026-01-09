<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<link rel="stylesheet" href="css/style.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

<style>
  .sr-only {
    position: absolute !important;
    width: 1px !important;
    height: 1px !important;
    padding: 0 !important;
    margin: -1px !important;
    overflow: hidden !important;
    clip: rect(0,0,0,0) !important;
    border: 0 !important;
  }
  .user-form-container { z-index: 10000; }
</style>

<c:if test="${not empty sessionScope.successo}">
  <div class="toast success-toast" id="toast-success" style="display: flex;">
    <span>${sessionScope.successo}</span>
    <i class="fas fa-times close-toast" onclick="this.parentElement.style.display='none'"></i>
  </div>
  <c:remove var="successo" scope="session" />
</c:if>

<c:if test="${not empty sessionScope.erroreLogin}">
  <div class="toast error-toast" id="toast-error" style="display: flex;">
    <span>${sessionScope.erroreLogin}</span>
    <i class="fas fa-times close-toast" onclick="this.parentElement.style.display='none'"></i>
  </div>
  <c:remove var="erroreLogin" scope="session" />
</c:if>

<div class="user-form-container" id="user-form-modal" style="display: none;">
  <div class="form-box">
    <span id="close-login-btn" class="fas fa-times"></span>

    <form id="login-form" action="LoginServlet" method="post" class="login-form">
      <h3>Accedi</h3>
      <label for="email-login" class="sr-only">Email</label>
      <input type="email" id="email-login" name="email" placeholder="Email" required class="box" pattern="^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$">
      <label for="password-login" class="sr-only">Password</label>
      <input type="password" id="password-login" name="password" placeholder="Password" required class="box" minlength="6">
      <input type="submit" value="Login" class="btn">
      <p style="text-align:center; margin-top: 10px;">
        Non hai un account? <a href="#" id="link-to-register">Registrati</a>
      </p>
    </form>

    <c:if test="${not empty errore}">
      <div class="toast error-toast">${errore}</div>
    </c:if>

    <form id="register-form" action="RegistrazioneServlet" method="post" class="register-form" style="display: none;">
      <h3>Registrati</h3>
      <label for="nome-reg" class="sr-only">Nome</label>
      <input type="text" id="nome-reg" name="nome" placeholder="Nome" required class="box">
      <label for="cognome-reg" class="sr-only">Cognome</label>
      <input type="text" id="cognome-reg" name="cognome" placeholder="Cognome" required class="box">
      <label for="email-reg" class="sr-only">Email</label>
      <input type="email" id="email-reg" name="email" placeholder="Email" required class="box" pattern="^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$">
      <label for="password-reg" class="sr-only">Password</label>
      <input type="password" id="password-reg" name="password" placeholder="Password" required class="box" minlength="6">
      <input type="submit" value="Registrati" class="btn">
      <p style="text-align:center; margin-top: 10px;">
        Hai già un account? <a href="#" id="link-to-login">Accedi</a>
      </p>
    </form>
  </div>
</div>

<header class="header white-bg">
  <div class="header-1">
    <a href="home" class="logo">
      <img src="${pageContext.request.contextPath}/images/LogoStellarLibraryNoBg.png" alt="Stellar Library">
      <span>Stellar Library</span>
    </a>

    <form action="" class="search-form" onsubmit="return false;">
      <input type="text" name="search" placeholder="Cerca il tuo libro..." id="search-box" autocomplete="off">
      <label for="search-box" class="fas fa-search"></label>
      <div id="autocomplete-list" class="autocomplete-items"></div>
    </form>

    <div class="user-actions-container">
      <c:choose>
        <c:when test="${not empty sessionScope.utente}">
          <div class="logged-in-wrapper">
            <div class="user-info-pill">
              <i class="fas fa-user-circle"></i>
              <span class="user-name-text">${sessionScope.utente.nome}</span>
            </div>
            <form action="LogoutServlet" method="post" class="logout-form-inline">
              <button type="submit" class="logout-icon-btn" title="Esci">
                <i class="fas fa-sign-out-alt"></i>
              </button>
            </form>
          </div>
        </c:when>
        <c:otherwise>
          <div id="login-btn-trigger" class="login-btn-pill" style="cursor: pointer;">
            <i class="fas fa-user"></i>
            <span>Accedi</span>
          </div>
        </c:otherwise>
      </c:choose>
    </div>
  </div>

  <div class="header-2">
    <div id="menu-btn" class="fas fa-bars"></div>

    <nav class="navbar">
      <a href="./">Home</a>

      <c:if test="${not empty sessionScope.utente}">
        <a href="VisualizzaOrdiniServlet">Ordini</a>
      </c:if>

      <a href="catalogo">Collezione</a>
      <a href="VisualizzaCarrelloServlet">Carrello</a>

      <c:if test="${sessionScope.utente.tipo == 'Admin'}">
        <div class="dropdown2">
          <a href="#" class="dropbtn">Dashboard <i class="fas fa-caret-down"></i></a>
          <div class="dropdown2-content">
            <a href="OrdiniUtentiServlet">Ordini utenti</a>
            <a href="GestioneRecensioniServlet">Recensioni</a>
            <a href="AggiuntaLibro">Aggiungi Libro</a>
          </div>
        </div>
      </c:if>
    </nav>
  </div>
</header>

<script>
  document.addEventListener('DOMContentLoaded', () => {

    /* -----------------------------------------------
       1. GESTIONE MENU HAMBURGER E NAVBAR
       ----------------------------------------------- */
    const menuBtn = document.querySelector('#menu-btn');
    const navbar = document.querySelector('.header .header-2 .navbar');

    if(menuBtn && navbar) {
      // Toggle menu al click dell'hamburger
      menuBtn.onclick = () => {
        menuBtn.classList.toggle('fa-times'); // Cambia icona in X
        navbar.classList.toggle('active');    // Mostra/Nascondi menu
      }

      // Chiudi menu quando si clicca un link qualsiasi dentro la navbar
      // (Include i link della dashboard)
      navbar.querySelectorAll('a').forEach(link => {
        link.addEventListener('click', () => {
          // Se è il trigger del dropdown (Dashboard), non chiudere subito su mobile
          // altrimenti non si vede il sottomenu
          if(link.classList.contains('dropbtn') && window.innerWidth <= 768) {
            return;
          }

          menuBtn.classList.remove('fa-times');
          navbar.classList.remove('active');
        });
      });
    }

    // Chiudi menu quando si scrolla la pagina
    window.onscroll = () => {
      if(menuBtn) menuBtn.classList.remove('fa-times');
      if(navbar) navbar.classList.remove('active');
    }

    /* -----------------------------------------------
       2. GESTIONE MODAL LOGIN (Esistente)
       ----------------------------------------------- */
    const loginBtn = document.getElementById('login-btn-trigger');
    const modal = document.getElementById('user-form-modal');
    const closeBtn = document.getElementById('close-login-btn');
    const linkToRegister = document.getElementById('link-to-register');
    const linkToLogin = document.getElementById('link-to-login');
    const loginForm = document.getElementById('login-form');
    const registerForm = document.getElementById('register-form');

    if(loginBtn) {
      loginBtn.onclick = () => {
        modal.style.display = 'flex';
        document.body.style.overflow = 'hidden';
      }
    }

    if(closeBtn) {
      closeBtn.onclick = () => {
        modal.style.display = 'none';
        document.body.style.overflow = 'auto';
      }
    }

    if(linkToRegister) {
      linkToRegister.onclick = (e) => {
        e.preventDefault();
        loginForm.style.display = 'none';
        registerForm.style.display = 'block';
      }
    }

    if(linkToLogin) {
      linkToLogin.onclick = (e) => {
        e.preventDefault();
        registerForm.style.display = 'none';
        loginForm.style.display = 'block';
      }
    }

    window.onclick = (event) => {
      if (event.target == modal) {
        modal.style.display = "none";
        document.body.style.overflow = 'auto';
      }
    }
  });
</script>

<script src="js/validazioni.js"></script>
<script src="js/autocomplete.js"></script>