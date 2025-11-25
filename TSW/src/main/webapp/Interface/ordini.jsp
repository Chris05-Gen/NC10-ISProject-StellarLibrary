<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>I miei ordini</title>
  <link rel="stylesheet" href="css/stileCheckout.css">
  <link rel="stylesheet" href="css/stileCarrello.css">
  <link rel="stylesheet" href="css/style.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

</head>
<body>

<jsp:include page="navbar.jsp" />

<h1 style="text-align:center;">📦 I miei ordini</h1>

<div class="checkout-container">

  <c:if test="${empty ordini}">
    <p style="text-align:center;">🚫 Nessun ordine effettuato.</p>
  </c:if>

  <c:forEach var="ordine" items="${ordini}">
    <c:set var="indirizzo" value="${mappaIndirizzi[ordine.id]}" />
    <c:set var="pagamento" value="${mappaPagamenti[ordine.id]}" />

    <div class="checkout-form">
      <h2>Ordine #${ordine.id}</h2>
      <table>
        <tbody>
        <tr>
          <th style="text-align: left;">📅 Data:</th>
          <td>${ordine.dataOrdine}</td>
        </tr>
        <tr>
          <th style="text-align: left;">📍 Spedizione:</th>
          <td>${indirizzo.via}, ${indirizzo.cap}, ${indirizzo.citta} (${indirizzo.provincia}) - ${indirizzo.nazione}</td>
        </tr>
        <tr>
          <th style="text-align: left;">📞 Tel:</th>
          <td>${indirizzo.telefono}</td>
        </tr>
        <tr>
          <th style="text-align: left;">💳 Pagamento:</th>
          <td>${pagamento.descrizione} </td>
        </tr>
        <tr>
          <th style="text-align: left;">💰 Totale:</th>
          <td>€ ${ordine.totale}</td>
        </tr>
        </tbody>
      </table>
    </div>
  </c:forEach>



</div>



</body>
</html>
