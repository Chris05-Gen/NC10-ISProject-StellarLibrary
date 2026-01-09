<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %> <html>
<head>
  <title>I miei ordini</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <link rel="stylesheet" href="css/stileOrdini.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

<jsp:include page="navbar.jsp" />

<div class="orders-container">
  <h1 class="page-title">📦 I miei ordini</h1>

  <c:choose>
    <%-- NESSUN ORDINE --%>
    <c:when test="${empty ordini}">
      <div class="empty-state">
        <i class="fas fa-box-open"></i>
        <h2>Non hai ancora effettuato ordini</h2>
        <p>Torna al catalogo per trovare la tua prossima lettura.</p>
        <a href="catalogo" class="btn" style="background: #008a00; color: white; padding: 10px 20px; border-radius: 4px; text-decoration: none; display: inline-block; margin-top: 15px;">Vai allo Shopping</a>
      </div>
    </c:when>

    <%-- LISTA ORDINI (CARD LAYOUT) --%>
    <c:otherwise>
      <c:forEach var="ordine" items="${ordini}">

        <div class="order-card">
          <div class="order-header">
            <div class="order-id">
              ORDINE #${ordine.id}
            </div>
            <div class="order-date">
              <i class="far fa-calendar-alt"></i>
              <fmt:formatDate value="${ordine.dataOrdine}" pattern="dd MMMM yyyy, HH:mm" />
            </div>
          </div>

          <div class="order-body">

            <div class="info-group">
              <div class="info-label"><i class="fas fa-map-marker-alt"></i> Spedizione</div>
              <div class="info-value">
                  ${ordine.indirizzo.via}<br>
                  ${ordine.indirizzo.cap} ${ordine.indirizzo.citta} (${ordine.indirizzo.provincia})<br>
                  ${ordine.indirizzo.nazione}
              </div>
              <div class="info-value" style="margin-top: 5px; font-size: 0.9rem; color: #666;">
                <i class="fas fa-phone" style="font-size: 0.8rem;"></i> ${ordine.indirizzo.telefono}
              </div>
            </div>

            <div class="info-group">
              <div class="info-label"><i class="fas fa-credit-card"></i> Pagamento</div>
              <div class="info-value">
                  ${ordine.metodoPagamento.descrizione} <br>
                <span style="font-size: 0.9rem; color: #666;">${ordine.metodoPagamento.circuito}</span>
              </div>
            </div>

            <div class="info-group" style="align-items: flex-start; justify-content: center;">
              <div class="info-label"><i class="fas fa-tag"></i> Totale</div>
              <div class="price-tag">€ ${ordine.totale}</div>
            </div>

          </div>
        </div>

      </c:forEach>
    </c:otherwise>
  </c:choose>
</div>

</body>
</html>