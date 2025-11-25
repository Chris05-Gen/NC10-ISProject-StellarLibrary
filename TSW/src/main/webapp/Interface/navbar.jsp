<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!-- TOAST (centrale) -->
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
</style>
<link rel="stylesheet" href="css/style.css">
<c:if test="${not empty sessionScope.successo}">
  <div class="toast success-toast" id="login-toast">
    <span>${sessionScope.successo}</span>
    <i class="fas fa-times close-toast" onclick="document.getElementById('login-toast').style.display='none'"></i>
  </div>
  <c:remove var="successo" scope="session" />
</c:if>
<c:if test="${not empty sessionScope.erroreLogin}">
  <div class="toast error-toast" id="login-toast">
    <span>${sessionScope.erroreLogin}</span>
    <i class="fas fa-times close-toast" onclick="document.getElementById('login-toast').style.display='none'"></i>
  </div>
  <c:remove var="erroreLogin" scope="session" />
</c:if>
<div class="user-form-container" id="userForm">
  <div class="form-box">
    <span id="close-form" class="fas fa-times"></span>

    <!-- Form Login -->
    <form id="login-form" action="LoginServlet" method="post" class="login-form">
      <h3>Accedi</h3>
      <label for="email-login" class="sr-only">Email</label>
      <input type="email" id="email-login" name="email" placeholder="Email" required class="box"
             pattern="^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$">
      <label for="password-login" class="sr-only">Password</label>
      <input type="password" id="password-login" name="password" placeholder="Password" required class="box" minlength="6">
      <input type="submit" value="Login" class="btn">
      <p style="text-align:center; margin-top: 10px;">
        Non hai un account? <a href="#" id="show-register">Registrati</a>
      </p>
    </form>

    <c:if test="${not empty errore}">
      <div class="toast error-toast">${errore}</div>
    </c:if>
    <!-- Form Registrazione -->
    <form id="register-form" action="RegistrazioneServlet" method="post" class="register-form" style="display: none;">
      <h3>Registrati</h3>
      <label for="nome-reg" class="sr-only">Nome</label>
      <input type="text" id="nome-reg" name="nome" placeholder="Nome" required class="box">
      <label for="cognome-reg" class="sr-only">Cognome</label>
      <input type="text" id="cognome-reg" name="cognome" placeholder="Cognome" required class="box">
      <label for="email-reg" class="sr-only">Email</label>
      <input type="email" id="email-reg" name="email" placeholder="Email" required class="box"
             pattern="^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$">
      <label for="password-reg" class="sr-only">Password</label>
      <input type="password" id="password-reg" name="password" placeholder="Password" required class="box" minlength="6">

      <input type="submit" value="Registrati" class="btn">
      <p style="text-align:center; margin-top: 10px;">
        Hai gia' un account? <a href="#" id="show-login">Accedi</a>
      </p>
    </form>
  </div>
</div>

<header class="header white-bg">
  <div class="header-1">
    <a href="home" class="logo navy">
      <i class="fas fa-book navy"></i> Stellar Library
    </a>

    <form action="" class="search-form" onsubmit="return false;">
      <input type="text" name="search" placeholder="Cerca qui..." id="search-box" autocomplete="off">
      <div id="autocomplete-list" class="autocomplete-items"></div>
    </form>


    <!-- USER ICON / NOME -->
    <div class="user-icon">
      <c:choose>
        <c:when test="${not empty sessionScope.utente}">
          <div class="user-dropdown">
            <span class="user-name">Ciao, ${sessionScope.utente.nome}</span>
            <div class="dropdown-content">
              <form action="LogoutServlet" method="post">
                <button type="submit" class="logout-btn">Logout</button>
              </form>
            </div>
          </div>
        </c:when>
        <c:otherwise>
          <i class="fas fa-user" id="user-btn"></i>
        </c:otherwise>
      </c:choose>
    </div>
  </div>

    <div class="header-2">
      <div id="menu-btn" class="fas fa-bars"></div>
      <nav class="navbar">
        <a href="home">Home</a>
        <c:if test="${not empty sessionScope.utente}">
          <a href="VisualizzaOrdiniServlet">Ordini</a>
        </c:if>
        <a href="catalogo">Collezione</a>
        <a href="VisualizzaCarrelloServlet">Carrello</a>
        <c:if test="${sessionScope.utente.tipo == 'Admin'}">
          <div class="dropdown2">
            <a href="#" class="dropbtn">Dashboard</a>
            <div class="dropdown2-content">
              <a href="controller/OrdiniUtentiServlet">Ordini utenti</a>
              <a href="controller/GestioneRecensioniServlet">Recensioni</a>
              <a href="controller/AggiuntaLibro">Aggiungi </a>
            </div>
          </div>
        </c:if>

    </nav>
  </div>
  <script src="js/validazioni.js"></script>
  <script src="js/autocomplete.js"></script>
</header>
