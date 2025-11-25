<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Gestione Recensioni</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

</head>
<body class="RecensioniUtenti">

<jsp:include page="navbar.jsp"/>

<c:if test="${not empty errore}">
    <div class="toast error-toast">${errore}</div>
</c:if>
<div class="recensioni-container">
    <h1>Recensioni Utenti</h1>

    <c:choose>
        <c:when test="${not empty recensioni}">
            <table>
                <thead>
                <tr>
                    <th scope="col">ID</th>
                    <th scope="col">Utente</th>
                    <th scope="col">Libro (ISBN)</th>
                    <th scope="col">Titolo Recensione</th>
                    <th scope="col">Testo</th>
                    <th scope="col">Valutazione</th>
                    <th scope="col">Data</th>
                    <th scope="col">Azioni</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="r" items="${recensioni}">
                    <tr>
                        <td>${r.id}</td>
                        <td>${r.nomeUtente}</td>
                        <td>${r.isbn}</td>
                        <td>${r.titolo}</td>
                        <td>${r.testo}</td>
                        <td>${r.valutazione}/5</td>
                        <td><fmt:formatDate value="${r.data}" pattern="dd/MM/yyyy"/></td>
                        <td>
                            <form method="post" action="GestioneRecensioniServlet">
                                <input type="hidden" name="idRecensione" value="${r.id}" />
                                <button type="submit" aria-label="Elimina la recensione di ${r.nomeUtente} su ${r.titolo}" onclick="return confirm('Eliminare questa recensione?')">Elimina</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <p>Nessuna recensione trovata.</p>
        </c:otherwise>
    </c:choose>
</div>

</body>
</html>
