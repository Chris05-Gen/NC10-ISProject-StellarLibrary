<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestione Ordini - Admin</title>

    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <%-- FIX CSS SPECIFICI PER QUESTA PAGINA --%>
    <style>
        @media (max-width: 768px) {
            /* 1. Forza la scritta "ORDINE" a essere BIANCA Pura */
            body.ordiniUtenti tbody td:first-child::before {
                color: #ffffff !important;
                opacity: 1 !important; /* Rimuove l'effetto trasparenza grigia */
                font-weight: 600 !important;
            }

            /* 2. Forza l'allineamento del TOTALE (Etichetta a sx, Prezzo a dx) */
            td[data-label="Totale"] {
                display: flex !important;
                justify-content: space-between !important;
                align-items: center !important;
            }
        }
    </style>
</head>
<body class="ordiniUtenti">

<jsp:include page="navbar.jsp" />

<div class="ordini-container">

    <c:if test="${not empty errore}">
        <div class="toast error-toast">${errore}</div>
    </c:if>

    <h1 class="page-title"><i class="fas fa-user-shield"></i> Pannello Ordini Utenti</h1>
    <p class="subtitle" style="text-align: center; color: #666; margin-bottom: 30px;">
        Riepilogo completo di tutti gli ordini
    </p>

    <c:choose>
        <c:when test="${not empty ordiniUtenti}">
            <div style="overflow-x: auto;">
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Data</th>
                        <th>Cliente</th>
                        <th>Indirizzo Spedizione</th>
                        <th>Pagamento</th>
                        <th>Totale</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="ordine" items="${ordiniUtenti}">
                        <tr>
                            <td data-label="ID" style="font-weight: bold; color: #fff; background-color: #191970;">
                                #${ordine.id}
                            </td>

                            <td data-label="Data">
                                <div style="text-align: right;">
                                    <span style="font-weight: 600; display: block;">
                                        <fmt:formatDate value="${ordine.dataOrdine}" pattern="dd/MM/yyyy"/>
                                    </span>
                                    <span style="font-size: 0.85em; color: #666;">
                                        <fmt:formatDate value="${ordine.dataOrdine}" pattern="HH:mm"/>
                                    </span>
                                </div>
                            </td>

                            <td data-label="Cliente">
                                <div style="text-align: right;">
                                    <strong>${ordine.utente.nome} ${ordine.utente.cognome}</strong><br>
                                    <small style="color: #888;">${ordine.utente.email}</small>
                                </div>
                            </td>

                            <td data-label="Indirizzo">
                                <c:choose>
                                    <c:when test="${not empty ordine.indirizzo}">
                                        <div style="text-align: right; line-height: 1.4;">
                                            <span style="display: block; font-weight: 600; color: #333;">
                                                    ${ordine.indirizzo.via}
                                            </span>
                                            <span style="color: #666; font-size: 0.9em;">
                                                ${ordine.indirizzo.citta} (${ordine.indirizzo.provincia})
                                            </span>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <em style="color: #999;">Non specificato</em>
                                    </c:otherwise>
                                </c:choose>
                            </td>

                            <td data-label="Pagamento">
                                <c:choose>
                                    <c:when test="${not empty ordine.metodoPagamento}">
                                        <span style="background: #eef; color: #191970; padding: 4px 8px; border-radius: 4px; font-size: 0.9em; font-weight: 600;">
                                                ${ordine.metodoPagamento.circuito}
                                        </span>
                                    </c:when>
                                    <c:otherwise>N/D</c:otherwise>
                                </c:choose>
                            </td>

                            <td data-label="Totale">
                                <strong style="color: #2e7d32; font-size: 1.2rem;">
                                    € <fmt:formatNumber value="${ordine.totale}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
                                </strong>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:when>

        <c:otherwise>
            <div style="text-align: center; padding: 50px; color: #666;">
                <i class="fas fa-box-open" style="font-size: 4rem; margin-bottom: 20px; color: #ddd;"></i>
                <h3>Nessun ordine trovato.</h3>
            </div>
        </c:otherwise>
    </c:choose>
</div>

</body>
</html>