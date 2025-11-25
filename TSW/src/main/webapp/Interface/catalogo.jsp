<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Catalogo Libri</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

</head>
<body>

<jsp:include page="navbar.jsp" />

<section class="catalogo-section">
    <div class="catalogo-container">
        <!-- Sidebar filtri -->
        <aside class="filtri">
            <form method="get" action="catalogo">
                <h3>Filtra</h3>
                <label for="genere">Genere:</label>
                <select id="genere" name="genere">
                    <option value="">Tutti</option>
                    <c:forEach var="g" items="${generi}">
                        <option value="${g.id}"
                                <c:if test="${selectedGenere == g.id}">selected</c:if>>
                                ${g.nome}
                        </option>
                    </c:forEach>
                </select>

                <!-- slider per pagine -->
                <label for="minPagine">Pagine minime: <output id="outPagine">${minPagine != null ? minPagine : 0}</output></label>
                <br>
                <input type="range" id="minPagine" name="minPagine" min="0" max="2000" step="10" value="${minPagine != null ? minPagine : 0}" oninput="outPagine.value = this.value">

                <br><br>
                <label for="anno">Anno:</label>
                <input type="number" name="anno" id="anno" min="1000" max="<%= java.time.Year.now().getValue() %>" pattern="[0-9]{4}" title="Inserisci un anno valido">
                <br><br>

                <label for="prezzoMin">Prezzo Min:</label>
                <input type="number" step="0.01" min="0" name="prezzoMin" id="prezzoMin" title="Prezzo minimo maggiore o uguale a 0">
                <br><br>

                <label for="prezzoMax">Prezzo Max:</label>
                <input type="number" step="0.01" min="0" name="prezzoMax" id="prezzoMax" title="Prezzo massimo maggiore o uguale a 0">
                <br><br>
                <button type="submit" class="btn">Applica</button>
            </form>
        </aside>

        <!-- Risultati libri -->
        <div class="risultati-libri">
            <c:if test="${empty libri}">
                <p>Nessun libro trovato.</p>
            </c:if>

            <c:forEach var="libro" items="${libri}">
                <div class="libro-box" tabindex="0" role="group" aria-label="Libro: ${libro.titolo} di ${libro.autore}">
                    <img src="${libro.copertina}" alt="Copertina del libro ${libro.titolo}" width="150">
                    <h3>${libro.titolo}</h3>
                    <p><strong>Autore:</strong> ${libro.autore}</p>
                    <p><strong>Prezzo:</strong> €${libro.prezzo}</p>
                    <a href="DettaglioLibroServlet?isbn=${libro.isbn}" class="btn" aria-label="Visualizza dettagli di ${libro.titolo}">Dettagli</a>
                </div>
            </c:forEach>
        </div>
    </div>
</section>
<script src="js/index.js"></script>
<script>
    const slider = document.getElementById('minPagine');
    const output = document.getElementById('outPagine');
    slider.addEventListener('input', () => {
        output.textContent = slider.value;
    });
</script>
</body>
</html>
