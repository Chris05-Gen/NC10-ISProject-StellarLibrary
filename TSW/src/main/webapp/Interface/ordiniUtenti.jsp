<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Ordini Utenti</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="css/style.css">

</head>
<body class="ordiniUtenti">

<jsp:include page="navbar.jsp" />
<c:if test="${not empty errore}">
    <div class="toast error-toast">${errore}</div>
</c:if>
<div class="ordini-container">
    <h1>Ordini degli Utenti</h1>
    <c:choose>
        <c:when test="${not empty ordiniUtenti}">
            <table aria-label="Elenco ordini degli utenti">
                <thead>
                <tr>
                    <th scope="col">ID Ordine</th>
                    <th scope="col">Utente</th>
                    <th scope="col">Data</th>
                    <th scope="col">Totale</th>
                    <th scope="col">Indirizzo</th>
                    <th scope="col">Metodo di Pagamento</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="o" items="${ordiniUtenti}">
                    <tr>
                        <td>${o.id}</td>
                        <td>${o.nome} ${o.cognome}</td>
                        <td><fmt:formatDate value="${o.dataOrdine}" pattern="dd/MM/yyyy HH:mm"/></td>
                        <td>€ ${o.totale}</td>
                        <td>${o.indirizzo.via}, ${o.indirizzo.cap} ${o.indirizzo.citta} (${o.indirizzo.provincia})</td>
                        <td>${o.metodoPagamento.circuito}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <p>Nessun ordine trovato.</p>
        </c:otherwise>
    </c:choose>
</div>

</body>
</html>
