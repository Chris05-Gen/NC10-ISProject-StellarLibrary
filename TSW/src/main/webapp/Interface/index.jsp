<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="it">
<head>
  
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Stellar Library</title>

  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

  <link rel="stylesheet" href="css/style.css">

</head>
<body>
<jsp:include page="navbar.jsp" /> <!-- navbar separata -->
<c:if test="${not empty errore}">
  <div class="toast error-toast">${errore}</div>
</c:if>
<!-- home section starts  -->
<section class="home white-bg navy" id="home">
  <center>
    <div class="row">
    <div class="content">
      <h3>Benvenuti nella Stellar Library</h3>
      <p>Scopri un universo di conoscenza con la nostra collezione di libri. Unisciti a noi e viaggia attraverso le pagine.</p>
    </div>
  </div>
  </center>

</section>
<!-- home section ends -->



<!-- featured section starts  -->

<!-- Swiper JS -->
<script src="https://cdn.jsdelivr.net/npm/swiper@9/swiper-bundle.min.js"></script>


  <section class="featured" id="featured">
    <h1 class="heading"> <span>Libri in evidenza</span> </h1>
    <div class="swiper featured-slider">
      <div class="swiper-wrapper">

        <c:forEach var="libro" items="${ultimiLibri}">
          <div class="swiper-slide box" role="group" aria-label="Libro: ${libro.titolo}">
            <div class="icons">
              <a href="AggiungiAlCarrelloServlet?isbn=${libro.isbn}&quantita=1" class="fas fa-shopping-cart"aria-label="Aggiungi ${libro.titolo} al carrello"
                 tabindex="0"></a>
              <a href="DettaglioLibroServlet?isbn=${libro.isbn}" class="fas fa-search" aria-label="Visualizza dettagli di ${libro.titolo}"
                 tabindex="0"></a>
            </div>
            <div class="image">
              <a href="DettaglioLibroServlet?isbn=${libro.isbn}"> <img src="${libro.copertina}" alt="Copertina del libro ${libro.titolo}"></a>
            </div>
            <div class="content">
              <h3>${libro.titolo}</h3>
            </div>
          </div>
        </c:forEach>



      </div>

      <!-- Bottoni frecce -->
      <div class="swiper-button-next" role="button" tabindex="0" aria-label="Vai al libro successivo">
        <img src="images/arrow.png" alt="Prossimo libro">
      </div>
      <div class="swiper-button-prev" role="button" tabindex="0" aria-label="Vai al libro precedente">
        <img src="images/arrow.png" alt="Libro precedente">
      </div>

    </div>
  </section>

<!-- featured section ends -->

<!-- footer section starts  -->
<section class="footer">
  <div class="box-container">
    <div class="box">
      <h3>Link veloci</h3>
      <a href="home">Home</a>
      <a href="#featured">Novità</a>
    </div>
    <div class="box">
      <h3>Contatti</h3>
      <a href="#"> <i class="fas fa-phone"></i> +39 123 456 789 </a>
      <a href="#"> <i class="fas fa-envelope"></i> info@stellarlibrary.it </a>
      <a href="#"> <i class="fas fa-map-marker-alt"></i> Napoli, Italia </a>
    </div>
  </div>
  <div class="credit"> Creato da <span>Stellar Devs</span> | Tutti i diritti riservati </div>
</section>
<!-- footer section ends -->

<!-- custom js file link  -->
<script src="js/index.js"></script>

</body>
</html>
