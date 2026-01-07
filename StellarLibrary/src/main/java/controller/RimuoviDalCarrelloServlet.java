package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Carrello;
import model.Utente;
import service.CarrelloService;

import java.io.IOException;

@WebServlet("/RimuoviDalCarrelloServlet")
public class RimuoviDalCarrelloServlet extends HttpServlet {

    private final CarrelloService carrelloService = new CarrelloService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utente");
        String isbn = request.getParameter("isbn");

        try {
            // 1️⃣ Recupero carrello (service gestisce validazioni e creazione)
            Carrello carrello = carrelloService.getOrCreateCarrello(utente, session);

            // 2️⃣ Delego al SERVICE la logica di rimozione + validazione input
            carrelloService.rimuoviLibroDalCarrello(carrello, isbn);

            // 3️⃣ Successo
            session.setAttribute("successo", "Libro rimosso correttamente");
            response.sendRedirect("VisualizzaCarrelloServlet");

        } catch (IllegalArgumentException e) {
            // Errori previsti → messaggio all'utente
            session.setAttribute("errore", e.getMessage());
            response.sendRedirect("VisualizzaCarrelloServlet");

        } catch (RuntimeException e) {
            // Errori inattesi → 500
            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Errore nella rimozione dal carrello"
            );
        }
    }
}

