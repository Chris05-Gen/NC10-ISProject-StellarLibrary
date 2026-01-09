<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"> <title>Gestione Recensioni</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body class="RecensioniUtenti">

<jsp:include page="navbar.jsp"/>

<c:if test="${not empty errore}">
    <div class="toast error-toast">
        <span>${errore}</span>
        <i class="fas fa-times close-toast" onclick="this.parentElement.style.display='none'"></i>
    </div>
</c:if>

<div class="recensioni-container">
    <h1><i class="fas fa-comments"></i> Recensioni Utenti</h1>

    <c:choose>
        <c:when test="${not empty recensioni}">
            <table>
                <thead>
                <tr>
                    <th scope="col" style="width: 50px;">ID</th>
                    <th scope="col" style="width: 120px;">Utente</th>
                    <th scope="col" style="width: 130px;">Libro (ISBN)</th>
                    <th scope="col">Titolo Recensione</th>
                    <th scope="col">Testo</th>
                    <th scope="col" style="width: 100px;">Voto</th>
                    <th scope="col" style="width: 110px;">Data</th>
                    <th scope="col" style="width: 80px;">Azioni</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="r" items="${recensioni}">
                    <tr>
                        <td data-label="ID">${r.utente.id}</td> <td data-label="Utente">
                        <strong>${r.nomeUtente}</strong>
                    </td>

                        <td data-label="ISBN">
                            <span style="font-family: monospace; color: #666;">${r.libro.isbn}</span>
                        </td>

                        <td data-label="Titolo" class="text-col" title="${r.titolo}">
                                ${r.titolo}
                        </td>

                        <td data-label="Testo" class="text-col" title="${r.testo}">
                                ${r.testo}
                        </td>

                        <td data-label="Valutazione">
                            <span style="color: #f39c12; font-weight: bold;">
                                ${r.valutazione} <i class="fas fa-star" style="font-size: 0.8em;"></i>
                            </span>
                        </td>

                        <td data-label="Data">
                            <fmt:formatDate value="${r.data}" pattern="dd/MM/yyyy"/>
                        </td>

                        <td data-label="Azioni">
                            <form method="post" action="GestioneRecensioniServlet" style="margin: 0;">
                                <input type="hidden" name="idRecensione" value="${r.id}" />
                                <button type="submit" class="btn-delete" title="Elimina Recensione"
                                        onclick="return confirm('Sei sicuro di voler eliminare questa recensione?')">
                                    <i class="fas fa-trash-alt"></i>
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <div style="text-align: center; padding: 40px; color: #666;">
                <i class="fas fa-inbox" style="font-size: 3rem; margin-bottom: 10px;"></i>
                <p>Nessuna recensione trovata nel database.</p>
            </div>
        </c:otherwise>
    </c:choose>
</div>

</body>
</html>