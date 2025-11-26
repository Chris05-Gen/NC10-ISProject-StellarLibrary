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

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String isbn = request.getParameter("isbn");

        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utente");

        try {
            Carrello carrello = carrelloService.getOrCreateCarrello(utente, session);

            if (carrello == null || carrello.getId() == -1) {
                response.sendRedirect("VisualizzaCarrelloServlet");
                return;
            }

            carrelloService.rimuoviLibroDalCarrello(carrello, isbn);

            session.setAttribute("successo", "Libro rimosso correttamente");
            response.sendRedirect("VisualizzaCarrelloServlet");

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore nella rimozione dal carrello");
        }
    }
}
