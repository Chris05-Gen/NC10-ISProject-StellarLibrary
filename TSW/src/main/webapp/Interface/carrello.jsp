    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <html>
    <head>
        <title>Carrello</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <link rel="stylesheet" href="css/stileCarrello.css">
        <link rel="stylesheet" href="css/style.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    </head>
    <body>

    <jsp:include page="navbar.jsp" /> <!-- navbar separata -->
    <h1>🛒 Il tuo carrello</h1>
    <br>
    <c:choose>
        <c:when test="${empty carrelloItems}">
            <p style="text-align: center;">Il carrello è vuoto.</p>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                <tr>
                    <th scope="col">Copertina</th>
                    <th scope="col">Titolo</th>
                    <th scope="col">Prezzo</th>
                    <th scope="col">Quantità</th>
                    <th scope="col">Subtotale</th>
                    <th scope="col">Azioni</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="item" items="${carrelloItems}">
                    <tr>
                        <td><a href="DettaglioLibroServlet?isbn=${item.libro.isbn}" class="img" aria-label="Visualizza dettagli di ${item.libro.titolo}"> <img src="${item.libro.copertina}" alt="Copertina del libro ${item.libro.titolo}"></a></td>
                        <td>${item.libro.titolo}</td>
                        <td>€ ${item.libro.prezzo}</td>
                        <td>${item.quantita}</td>
                        <td>€ ${item.subTotale}</td>
                        <td>
                            <form action="RimuoviDalCarrelloServlet" method="post">
                                <input type="hidden" name="isbn" value="${item.libro.isbn}" />
                                <button type="submit" aria-label="Rimuovi ${item.libro.titolo} dal carrello"><i class="fas fa-trash-alt"></i> Rimuovi</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <h3 style="text-align: center;">Totale: € ${totale}</h3>

            <c:choose>
                <c:when test="${not empty sessionScope.utente}">
                    <a href="CheckoutServlet" class="btn-checkout">Procedi al pagamento</a>
                </c:when>
                <c:otherwise>
                    <a href="#" id="open-login-from-cart" class="btn-checkout">Effettua il login per acquistare</a>
                </c:otherwise>
            </c:choose>
        </c:otherwise>
    </c:choose>
    <c:choose>
    <c:when test="${not empty carrelloItems}">
    <form action="SvuotaCarrelloServlet" method="post" style="text-align: center; margin-top: 20px;">
        <button type="submit" class="btn-checkout" style="background-color: #d9534f;" aria-label="Svuota tutto il carrello">Svuota Carrello</button>
    </form>
    </c:when>
    </c:choose>
    <script src="js/index.js"></script>
    </body>
    </html>
