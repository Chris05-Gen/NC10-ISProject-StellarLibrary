<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Gestione Ordini - Admin</title>

    <link rel="stylesheet" href="css/stileOrdini.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="ordiniUtenti">

<jsp:include page="navbar.jsp" />

<div class="orders-container">

    <c:if test="${not empty errore}">
        <div class="toast error-toast">${errore}</div>
    </c:if>

    <h1 class="page-title"><i class="fas fa-user-shield"></i> Pannello Ordini Utenti</h1>

    <c:choose>
        <c:when test="${not empty ordiniUtenti}">

            <div class="table-responsive">
                <table class="admin-table" aria-label="Elenco ordini degli utenti">
                    <thead>
                    <tr>
                        <th scope="col">ID</th>
                        <th scope="col">Cliente</th>
                        <th scope="col">Data</th>
                        <th scope="col">Indirizzo</th>
                        <th scope="col">Pagamento</th>
                        <th scope="col">Totale</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="o" items="${ordiniUtenti}">
                        <tr>
                            <td><strong>#${o.id}</strong></td>
                            <td>
                                <div class="user-info">
                                    <i class="fas fa-user-circle" style="color: #ccc;"></i>
                                        ${o.utente.nome} ${o.utente.cognome}
                                </div>
                                <small style="color: #888;">${o.utente.email}</small> </td>
                            <td>
                                <fmt:formatDate value="${o.dataOrdine}" pattern="dd/MM/yyyy"/>
                                <br>
                                <small style="color:#888;"><fmt:formatDate value="${o.dataOrdine}" pattern="HH:mm"/></small>
                            </td>
                            <td>
                                    ${o.indirizzo.citta} (${o.indirizzo.provincia})<br>
                                <small style="color: #888;">${o.indirizzo.via}</small>
                            </td>
                            <td>
                                <span class="badge">${o.metodoPagamento.circuito}</span>
                            </td>
                            <td>
                                <strong style="color: #008a00;">€ ${o.totale}</strong>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

        </c:when>
        <c:otherwise>
            <div class="empty-state">
                <i class="fas fa-clipboard-list"></i>
                <h3>Nessun ordine trovato nel sistema.</h3>
            </div>
        </c:otherwise>
    </c:choose>
</div>

</body>
</html>