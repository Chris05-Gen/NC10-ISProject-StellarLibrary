<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0"> <title>Aggiungi Libro - Stellar Library</title>
  <link rel="stylesheet" href="css/style.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body class="AggiuntaLibro">

<jsp:include page="navbar.jsp"/>

<div class="form-container">
  <h1><i class="fas fa-book-medical"></i> Aggiungi Nuovo Libro</h1>

  <c:if test="${param.success == '1'}">
    <div class="success">
      <i class="fas fa-check-circle"></i> Libro aggiunto con successo alla collezione!
    </div>
  </c:if>

  <c:if test="${not empty erroreLibro}">
    <div class="toast error-toast" id="error-toast" style="display: flex;">
      <span>${erroreLibro}</span>
      <i class="fas fa-times close-toast" onclick="this.parentElement.style.display='none'"></i>
    </div>
    <c:remove var="erroreLibro" scope="session" />
  </c:if>

  <form method="post" action="AggiuntaLibro">
    <div class="form-group">
      <label for="isbn">ISBN <small>(10 o 13 cifre)</small></label>
      <input type="text" id="isbn" name="isbn" required pattern="\d{10,13}" placeholder="Es. 9788804728563">
    </div>
    <div class="form-group">
      <label for="titolo">Titolo</label>
      <input type="text" id="titolo" name="titolo" required placeholder="Inserisci il titolo del libro">
    </div>

    <div class="form-group">
      <label for="autore">Autore</label>
      <input type="text" id="autore" name="autore" required placeholder="Nome e Cognome autore">
    </div>
    <div class="form-group">
      <label for="casaEditrice">Casa Editrice</label>
      <input type="text" id="casaEditrice" name="casaEditrice" required placeholder="Es. Mondadori">
    </div>

    <div class="form-group">
      <label for="pagine">Pagine</label>
      <input type="number" id="pagine" name="pagine" min="1" max="5000" required placeholder="Es. 350">
    </div>
    <div class="form-group">
      <label for="annoPubblicazione">Anno Pubblicazione</label>
      <input type="number" id="annoPubblicazione" name="annoPubblicazione" min="1000" max="2099" required placeholder="Es. 2024">
    </div>

    <div class="form-group">
      <label for="prezzo">Prezzo (€)</label>
      <input type="number" id="prezzo" name="prezzo" min="0.01" step="0.01" required placeholder="0.00">
    </div>
    <div class="form-group">
      <label for="idGenere">Genere</label>
      <select id="idGenere" name="idGenere" required>
        <option value="" disabled selected>-- Seleziona un genere --</option>
        <c:forEach var="g" items="${generi}">
          <option value="${g.id}">${g.nome}</option>
        </c:forEach>
      </select>
    </div>

    <div class="form-group full">
      <label for="copertina">URL Copertina</label>
      <input type="url" id="copertina" name="copertina" pattern="https?://.+" title="Inserisci un URL valido che inizia con http o https" placeholder="https://...">
    </div>

    <div class="form-group full">
      <button type="submit">
        <i class="fas fa-plus"></i> Aggiungi al Catalogo
      </button>
    </div>
  </form>
</div>

</body>
</html>