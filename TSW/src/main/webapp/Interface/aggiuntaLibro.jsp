<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="UTF-8">
  <title>Aggiungi Libro</title>
  <link rel="stylesheet" href="css/style.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

</head>
<body class="AggiuntaLibro">
<jsp:include page="navbar.jsp"/>

<div class="form-container">
  <h1>Aggiungi Nuovo Libro</h1>
  <c:if test="${param.success == '1'}">
    <p class="success">Libro aggiunto con successo 🎉</p>
  </c:if>
  <c:if test="${not empty errore}">
    <div class="toast error-toast">${errore}</div>
  </c:if>
  <form method="post" action="AggiuntaLibro">
    <div class="form-group">
      <label for="isbn">ISBN</label>
      <input type="text" id="isbn" name="isbn" required pattern="\d{10,13}" title="Inserisci 10 o 13 cifre numeriche">
    </div>
    <div class="form-group">
      <label for="titolo">Titolo</label>
      <input type="text" id="titolo" name="titolo" required>
    </div>
    <div class="form-group">
      <label for="autore">Autore</label>
      <input type="text" id="autore" name="autore" required>
    </div>
    <div class="form-group">
      <label for="casaEditrice">Casa Editrice</label>
      <input type="text" id="casaEditrice" name="casaEditrice" required>
    </div>
    <div class="form-group">
      <label for="pagine">Pagine</label>
      <input type="number" id="pagine" name="pagine" min="1" max="5000" required>
    </div>
    <div class="form-group">
      <label for="copertina">Copertina (URL)</label>
      <input type="url" id="copertina" name="copertina" pattern="https?://.+" title="Inserisci un URL valido">
    </div>
    <div class="form-group">
      <label for="annoPubblicazione">Anno Pubblicazione</label>
      <input type="number" id="annoPubblicazione" name="annoPubblicazione" min="1000" max="2099" required>
    </div>
    <div class="form-group">
      <label for="prezzo">Prezzo (€)</label>
      <input type="number" id="prezzo" name="prezzo" min="0.01" step="0.01" required>
    </div>
    <div class="form-group">
      <label for="idGenere">Genere</label>
      <select id="idGenere" name="idGenere" required>
        <option value="">-- seleziona --</option>
        <c:forEach var="g" items="${generi}">
          <option value="${g.id}">${g.nome}</option>
        </c:forEach>
      </select>
    </div>
    <div class="form-group full">
      <button type="submit">📚 Aggiungi Libro</button>
    </div>
  </form>
</div>
</body>
</html>
