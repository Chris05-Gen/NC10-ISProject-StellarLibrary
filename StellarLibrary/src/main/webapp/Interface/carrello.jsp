<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Il tuo carrello</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="stylesheet" href="css/stileCarrello.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

<jsp:include page="navbar.jsp" />

<div class="main-wrapper">

    <c:choose>
        <%-- CASO 1: CARRELLO VUOTO --%>
        <c:when test="${empty carrelloItems}">
            <div style="width: 100%; text-align: center; padding: 50px; background: white;">
                <h2>Il tuo carrello è vuoto</h2>
                <p>Non hai ancora aggiunto articoli.</p>
                <a href="./" class="btn-checkout" style="max-width: 200px; margin: 20px auto;">Inizia lo shopping</a>
            </div>
        </c:when>

        <%-- CASO 2: CARRELLO PIENO --%>
        <c:otherwise>

            <div class="cart-list-section">
                <div class="cart-header-title">Il tuo carrello (${carrelloItems.size()})</div>

                <c:forEach var="item" items="${carrelloItems}">
                    <div class="product-card">

                        <a href="DettaglioLibroServlet?isbn=${item.libro.isbn}">
                            <img src="${item.libro.copertina}" alt="${item.libro.titolo}" class="product-img">
                        </a>

                        <div class="product-details">
                            <div>
                                <div class="product-category">LIBRI</div>
                                <a href="DettaglioLibroServlet?isbn=${item.libro.isbn}" class="product-title">
                                        ${item.libro.titolo}
                                </a>
                                <div class="product-status">
                                    <i class="fas fa-check-circle"></i> Disponibilità immediata
                                </div>
                            </div>

                            <div class="product-actions">
                                <form action="RimuoviDalCarrelloServlet" method="post" style="margin:0;">
                                    <input type="hidden" name="isbn" value="${item.libro.isbn}" />
                                    <button type="submit" class="action-btn trash" title="Rimuovi">
                                        <i class="fas fa-trash-alt"></i>
                                    </button>
                                </form>
                            </div>
                        </div>

                        <div class="product-price-box">
                            <div class="current-price">€ ${item.libro.prezzo}</div>
                            <div class="qty-display">Qtà: ${item.quantita}</div>
                        </div>
                    </div>
                </c:forEach>

                <form action="SvuotaCarrelloServlet" method="post">
                    <button type="submit" class="btn-empty-cart" onclick="return confirm('Svuotare il carrello?');">
                        <i class="fas fa-times"></i> Svuota intero carrello
                    </button>
                </form>
            </div>

            <div class="cart-summary-section">
                <div class="summary-box">
                    <div class="summary-title">Riepilogo ordine</div>

                    <div class="summary-row">
                        <span>Subtotale articoli</span>
                        <span>€ ${totale}</span>
                    </div>

                    <div class="summary-row">
                        <span>Spese di spedizione</span>
                        <span style="color: #27ae60;">Gratis</span>
                    </div>

                    <div class="summary-row total">
                        <span>Totale</span>
                        <span>€ ${totale}</span>
                    </div>

                    <c:choose>
                        <c:when test="${not empty sessionScope.utente}">
                            <a href="CheckoutServlet" class="btn-checkout">Acquista ora</a>
                        </c:when>
                        <c:otherwise>
                            <a href="login.jsp" class="btn-checkout">Acquista ora</a>
                            <div class="login-notice">
                                <i class="fas fa-info-circle"></i> Effettua il login per completare l'acquisto.
                            </div>
                        </c:otherwise>
                    </c:choose>

                </div>
            </div>

        </c:otherwise>
    </c:choose>
</div>

<script src="js/index.js?v=${pageContext.session.id}"></script>
</body>
</html>