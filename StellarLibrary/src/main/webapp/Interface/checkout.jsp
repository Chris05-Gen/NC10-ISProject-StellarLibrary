<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
  <title>Checkout Sicuro</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <link rel="stylesheet" href="css/style.css">
  <link rel="stylesheet" href="css/stileCheckout.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

<jsp:include page="navbar.jsp" />

<h1 class="page-title">Completa il tuo ordine</h1>

<div class="checkout-wrapper">

  <div class="checkout-main">

    <form action="CreaOrdineServlet" method="post" id="main-order-form">

      <div class="checkout-section">
        <div class="section-title">
          <i class="fas fa-map-marker-alt"></i> Indirizzo di spedizione
        </div>

        <div class="address-grid">
          <c:if test="${empty indirizzi}">
            <p style="color: #c0392b; text-align: center; padding: 10px;">
              <i class="fas fa-exclamation-triangle"></i> Nessun indirizzo salvato.
            </p>
          </c:if>

          <c:forEach var="addr" items="${indirizzi}" varStatus="status">
            <label class="address-card">
              <input type="radio" name="idIndirizzo" value="${addr.id}" ${status.first ? 'checked' : ''} required>

              <div class="address-content">
                <div class="address-details">
                  <span class="addr-main">${addr.via}</span>
                  <span class="addr-sub">${addr.cap} ${addr.citta} (${addr.provincia}) - ${addr.nazione}</span>
                  <span class="addr-phone"><i class="fas fa-phone-alt"></i> ${addr.telefono}</span>
                </div>
                <i class="fas fa-check-circle check-icon"></i>
              </div>
            </label>
          </c:forEach>
        </div>

        <button type="button" id="btn-toggle-indirizzo" class="btn-add-addr">
          <i class="fas fa-plus"></i> Aggiungi nuovo indirizzo
        </button>
      </div>

      <div class="checkout-section">
        <div class="section-title">
          <i class="fas fa-credit-card"></i> Metodo di pagamento
        </div>

        <div class="payment-grid">
          <c:forEach var="metodo" items="${metodiPagamento}" varStatus="status">
            <c:set var="nome" value="${metodo.circuito.toLowerCase()}" />
            <c:set var="isCard" value="${nome.contains('visa') || nome.contains('master') || nome.contains('amex') || nome.contains('maestro') || nome.contains('poste')}" />

            <label class="payment-card">
              <input type="radio"
                     name="metodoPagamento"
                     value="${metodo.id}"
                     data-is-card="${isCard}"
                ${status.first ? 'checked' : ''}
                     required>

              <div class="payment-content">
                <c:choose>
                  <c:when test="${nome.contains('visa')}">
                    <i class="fab fa-cc-visa" style="color:#1a1f71;"></i>
                  </c:when>
                  <c:when test="${nome.contains('master')}">
                    <i class="fab fa-cc-mastercard" style="color:#eb001b;"></i>
                  </c:when>
                  <c:when test="${nome.contains('amex')}">
                    <i class="fab fa-cc-amex" style="color:#2e77bc;"></i>
                  </c:when>
                  <c:when test="${nome.contains('paypal')}">
                    <i class="fab fa-paypal" style="color:#003087;"></i>
                  </c:when>
                  <c:when test="${nome.contains('apple')}">
                    <i class="fab fa-apple-pay" style="color:#000;"></i>
                  </c:when>
                  <c:when test="${nome.contains('google')}">
                    <i class="fab fa-google-pay" style="color:#4285F4;"></i>
                  </c:when>
                  <c:when test="${nome.contains('maestro')}">
                    <i class="fab fa-cc-mastercard" style="color:#0099cc;"></i> </c:when>
                  <c:when test="${nome.contains('poste')}">
                    <i class="fas fa-credit-card" style="color:#ffcc00;"></i> </c:when>
                  <c:when test="${nome.contains('sepa') || nome.contains('bonifico')}">
                    <i class="fas fa-university" style="color:#555;"></i>
                  </c:when>
                  <c:otherwise>
                    <i class="fas fa-wallet" style="color:#777;"></i>
                  </c:otherwise>
                </c:choose>

                <span class="payment-name">${metodo.circuito}</span>
              </div>
            </label>
          </c:forEach>
        </div>

        <div id="card-details-section">
          <h3><i class="fas fa-lock"></i> Inserisci i dati della carta</h3>
          <div class="card-form-grid">

            <div class="form-group">
              <label>Intestatario Carta</label>
              <input type="text" name="intestatarioCarta" class="card-input" placeholder="Mario Rossi">
            </div>

            <div class="form-group" style="grid-column: 1 / -1;">
              <label>Numero Carta</label>
              <input type="text" name="numeroCarta" class="card-input" placeholder="0000 0000 0000 0000" maxlength="19">
            </div>

            <div class="form-group">
              <label>Scadenza</label>
              <input type="text" name="scadenzaCarta" class="card-input" placeholder="MM/YY" maxlength="5">
            </div>

            <div class="form-group">
              <label>CVV <i class="fas fa-question-circle" title="3 cifre sul retro"></i></label>
              <input type="text" name="cvvCarta" class="card-input" placeholder="123" maxlength="3">
            </div>
          </div>
          <p style="font-size: 0.8rem; color: #666; margin-top: 10px;">
            I dati sono protetti e crittografati. Non salviamo il codice CVV.
          </p>
        </div>

      </div> </form>

    <div id="form-aggiungi-indirizzo" class="checkout-section" style="display: none;">
      <div class="section-title"><i class="fas fa-plus-circle"></i> Nuovo Indirizzo</div>
      <form action="AggiungiIndirizzoServlet" method="post">
        <div class="form-group"><label>Via</label><input type="text" name="via" required></div>
        <div class="form-row">
          <div class="form-group"><label>Città</label><input type="text" name="citta" required></div>
          <div class="form-group"><label>CAP</label><input type="text" name="cap" required></div>
        </div>
        <div class="form-row">
          <div class="form-group"><label>Provincia</label><input type="text" name="provincia" required></div>
          <div class="form-group"><label>Nazione</label><input type="text" name="nazione" required value="Italia"></div>
        </div>
        <div class="form-group"><label>Telefono</label><input type="text" name="telefono" required></div>
        <div style="display: flex; gap: 10px; margin-top: 15px;">
          <button type="submit" class="btn-confirm" style="margin-top:0; width: auto; flex: 1;">Salva</button>
          <button type="button" id="btn-annulla-indirizzo" class="btn-secondary">Annulla</button>
        </div>
      </form>
    </div>

  </div>

  <div class="checkout-sidebar">
    <div class="checkout-section summary-sticky">
      <div class="section-title">Riepilogo Ordine</div>

      <div class="summary-items">
        <c:forEach var="item" items="${carrelloItems}">
          <div class="summary-item">
            <img src="${item.libro.copertina}" alt="Cover">
            <div class="item-info" style="flex: 1;">
              <h4>${item.libro.titolo}</h4>
              <div class="item-meta">
                <span>Qtà: ${item.quantita}</span>
                <strong>€ ${item.subTotale}</strong>
              </div>
            </div>
          </div>
        </c:forEach>
      </div>

      <div class="summary-totals">
        <div class="summary-row">
          <span>Subtotale</span>
          <span>€ ${totale}</span>
        </div>
        <div class="summary-row">
          <span>Spedizione</span>
          <span style="color: var(--primary-color);">Gratis</span>
        </div>
        <div class="summary-row total">
          <span>Totale</span>
          <span>€ ${totale}</span>
        </div>
      </div>

      <button type="submit" form="main-order-form" class="btn-confirm">
        Conferma e Paga <i class="fas fa-lock" style="margin-left: 5px; font-size: 0.9rem;"></i>
      </button>

      <div style="text-align: center; margin-top: 15px; font-size: 0.8rem; color: #888;">
        <i class="fas fa-shield-alt"></i> Pagamenti sicuri e crittografati SSL
      </div>
    </div>
  </div>

</div>

<script>
  document.addEventListener("DOMContentLoaded", function() {

    // --- LOGICA GESTIONE INDIRIZZO (Quella che avevi già) ---
    const btnOpen = document.getElementById("btn-toggle-indirizzo");
    const btnClose = document.getElementById("btn-annulla-indirizzo");
    const formAddr = document.getElementById("form-aggiungi-indirizzo");
    const btnMainAdd = document.getElementById("btn-toggle-indirizzo");

    if(btnOpen && formAddr) {
      btnOpen.addEventListener("click", function() {
        formAddr.style.display = "block";
        btnMainAdd.style.display = "none";
        formAddr.scrollIntoView({ behavior: 'smooth' });
      });
    }
    if(btnClose && formAddr) {
      btnClose.addEventListener("click", function() {
        formAddr.style.display = "none";
        btnMainAdd.style.display = "flex";
      });
    }

    // --- NUOVA LOGICA GESTIONE PAGAMENTO (MOSTRA/NASCONDI FORM CARTA) ---
    const paymentRadios = document.querySelectorAll('input[name="metodoPagamento"]');
    const cardDetailsSection = document.getElementById('card-details-section');
    const cardInputs = cardDetailsSection.querySelectorAll('input');

    function toggleCardForm() {
      // Cerca quale radio è selezionato
      const selected = document.querySelector('input[name="metodoPagamento"]:checked');

      if (selected && selected.getAttribute('data-is-card') === 'true') {
        // Se è una carta, mostra il form
        cardDetailsSection.style.display = 'block';

        // Rendi i campi "required" così il form non parte se sono vuoti
        cardInputs.forEach(input => input.setAttribute('required', 'required'));
      } else {
        // Altrimenti nascondi (es. PayPal)
        cardDetailsSection.style.display = 'none';

        // Rimuovi "required" altrimenti non puoi inviare l'ordine PayPal
        cardInputs.forEach(input => input.removeAttribute('required'));
      }
    }

    // Aggiungi listener a tutti i radio button
    paymentRadios.forEach(radio => {
      radio.addEventListener('change', toggleCardForm);
    });

    // Esegui al caricamento (nel caso uno sia già selezionato di default)
    toggleCardForm();
  });
</script>

</body>
</html>