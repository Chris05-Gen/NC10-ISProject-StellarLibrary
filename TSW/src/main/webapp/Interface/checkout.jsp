  <%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  <html>
  <head>
    <title>Checkout</title>

    <link rel="stylesheet" href="css/stileCheckout.css">
    <link rel="stylesheet" href="css/stileCarrello.css">
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
  </head>
  <body>
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


  <jsp:include page="navbar.jsp" />
  <c:if test="${not empty sessionScope.errore}">
    <div class="toast error-toast">
      <span>${sessionScope.errore}</span>
    </div>
    <c:remove var="errore" scope="session"/>
  </c:if>

  <c:if test="${not empty sessionScope.successo}">
    <div class="toast success-toast">
      <span>${sessionScope.successo}</span>
    </div>
    <c:remove var="successo" scope="session"/>
  </c:if>

  <h1 style="text-align:center;">🧾 Checkout</h1>

  <div class="checkout-container">

    <!-- 🛒 RIEPILOGO CARRELLO -->
    <h2>Riepilogo Ordine</h2>
    <c:if test="${not empty carrelloItems}">
      <table>
        <thead>
        <tr>
          <th>Copertina</th>
          <th>Titolo</th>
          <th>Quantità</th>
          <th>Subtotale</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="item" items="${carrelloItems}">
          <tr>
            <td><img src="${item.libro.copertina}" alt="${item.libro.titolo}" width="50"></td>
            <td>${item.libro.titolo}</td>
            <td>${item.quantita}</td>
            <td>€ ${item.subTotale}</td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
      <h3 style="text-align:right;">Totale: € ${totale}</h3>
    </c:if>

    <!-- 📦 FORM CHECKOUT -->
    <c:if test="${not empty carrelloItems}">
      <form action="CreaOrdineServlet" method="post" class="checkout-form">

        <!-- 📍 Indirizzo -->
        <h2>Indirizzo di spedizione</h2>

        <c:if test="${empty indirizzi}">
          <p>⚠️ Nessun indirizzo registrato.</p>
        </c:if>
        <c:if test="${not empty indirizzi}">
          <c:forEach var="addr" items="${indirizzi}">
            <label class="radio-block">
              <input type="radio" name="idIndirizzo" value="${addr.id}" required>
                ${addr.via}, ${addr.cap}, ${addr.citta} (${addr.provincia}) - ${addr.nazione}
              📞 ${addr.telefono}
            </label>
          </c:forEach>
        </c:if>

        <!-- Bottone sempre visibile -->
        <button id="btn-aggiungi-indirizzo" class="btn-checkout" type="button">➕ Aggiungi indirizzo</button>

        <!-- Se nessun indirizzo -->


        <hr>


        <!-- 💳 Metodo di pagamento -->
        <h2>Metodo di pagamento</h2>
        <label for="metodoPagamento" class="sr-only">Metodo di pagamento</label>
        <select name="metodoPagamento" id="metodoPagamento" required aria-label="Metodo di pagamento">
              <c:forEach var="metodo" items="${metodiPagamento}">
                  <option value="${metodo.id}">${metodo.circuito}</option>
              </c:forEach>
          </select>
        <br>
        <button type="submit" class="btn-checkout">Conferma Ordine</button>
      </form>
    </c:if>

    <!-- ✅ FORM AGGIUNGI INDIRIZZO (fuori dal form principale) -->
    <div id="form-aggiungi-indirizzo" style="display:none; margin-top:20px;">
      <form action="AggiungiIndirizzoServlet" method="post" class="checkout-form">
        <h3>Aggiungi un nuovo indirizzo</h3>
        <label for="via">Via: <input type="text" name="via" id="via" required /></label><br/>
        <label for="citta">Città: <input type="text" name="citta" id="citta" required /></label><br/>
        <label for="cap">CAP: <input type="text" name="cap" id="cap" required pattern="\d{5}" /></label><br/>
        <label for="provincia">Provincia: <input type="text" name="provincia" id="provincia" required /></label><br/>
        <label for="nazione">Nazione: <input type="text" name="nazione" id="nazione" required /></label><br/>
        <label for="telefono">Telefono:
          <input type="text" name="telefono" id="telefono" required pattern="\d{10}" title="Inserisci 10 cifre" maxlength="10" />
        </label>

        <div class="form-buttons">
          <button type="submit" class="btn-checkout">Salva indirizzo</button>
          <button type="button" id="btn-annulla-indirizzo" class="btn-secondary">❌</button>
        </div>
      </form>
    </div>

  </div>
  <script src="js/validazioni.js"></script>
  <script src="js/index.js"></script>
  </body>
  </html>
