<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>${libro.titolo}</title>
  <link rel="stylesheet" href="css/style.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
  <link rel="stylesheet" href="css/stileLibro.css">


</head>
<body>


<jsp:include page="navbar.jsp" /> <!-- Se hai la navbar separata -->

<div class="container">
  <div class="book-layout">
    <!-- Immagine libro -->
    <div class="book-image">
      <img src="${libro.copertina}" alt="${libro.titolo}" />
    </div>

    <!-- Dettagli libro -->
    <div class="book-info">
      <h1>${libro.titolo}</h1>
      <p><strong>Autore:</strong> ${libro.autore}</p>
      <p><strong>ISBN:</strong> ${libro.isbn}</p>
      <p><strong>Genere:</strong> ${libro.genere.nome}</p>
      <p><strong>Editore:</strong> ${libro.casaEditrice}</p>
      <p><strong>Pagine:</strong> ${libro.pagine}</p>
      <p><strong>Anno pubblicazione:</strong> ${libro.annoPubblicazione}</p>

      <div class="book-price">
        <span>€${libro.prezzo}</span>
        <form action="AggiungiAlCarrelloServlet" method="post">
          <input type="hidden" name="isbn" value="${libro.isbn}">
          <label for="quantita">Quantità:</label>
          <input type="number" name="quantita"  id="quantita" value="1" min="1" max="99" />
          <button type="submit" class="add-to-cart-btn">Aggiungi al carrello</button>
        </form>
      </div>
    </div>
  </div>

  <!-- Recensioni -->
  <div class="recensioni-section">
    <h2>Recensioni</h2>
    <c:choose>
      <c:when test="${not empty recensioni}">
        <ul>
          <c:forEach var="r" items="${recensioni}">
            <li>
              <strong>${r.titolo}</strong> – <em>${r.valutazione}/5</em><br>
              <p>${r.testo}</p>
              <span class="recensione-data">
                  <fmt:formatDate value="${r.data}" pattern="dd/MM/yyyy" />
              </span>
            </li>
          </c:forEach>
        </ul>
      </c:when>
      <c:otherwise>
        <p>Nessuna recensione disponibile.</p>
      </c:otherwise>
    </c:choose>
  </div>
</div>
<!-- Form per lasciare una nuova recensione -->
<c:if test="${not empty errore}">
  <div class="toast error-toast">${errore}</div>
</c:if>
<c:if test="${not empty sessionScope.utente}">
    <div class="recensione-form-container">
      <h3>Lascia una recensione</h3>
      <form action="AggiungiRecensioneServlet" method="post" class="recensione-form">
        <input type="hidden" name="isbn" value="${libro.isbn}" />

        <div class="form-group">
          <label for="titolo">Titolo:</label>
          <input type="text" name="titolo" id="titolo" required maxlength="50" />
        </div>

        <div class="form-group">
          <label for="testo">Testo:</label>
          <textarea name="testo" id="testo" rows="4" required maxlength="500"
                    placeholder="Scrivi una recensione utile e rispettosa..."></textarea>
        </div>

        <div class="form-group">
          <label for="valutazione">Valutazione (1-5):</label>
          <select name="valutazione" id="valutazione" required>
            <c:forEach begin="1" end="5" var="i">
              <option value="${i}">${i}</option>
            </c:forEach>
          </select>
        </div>

        <button type="submit">Invia Recensione</button>
      </form>
    </div>

</c:if>




<script src="js/index.js"></script>

</body>
</html>