package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Carrello;
import model.Utente;
import service.CarrelloService;

import java.io.IOException;

@WebServlet("/SvuotaCarrelloServlet")
public class SvuotaCarrelloServlet extends HttpServlet {
    private final CarrelloService carrelloService = new CarrelloService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utente");

        try {
            Carrello carrello = carrelloService.getOrCreateCarrello(utente, session);

            if (carrello == null || carrello.getId() == -1) {
                response.sendRedirect("VisualizzaCarrelloServlet");
                return;
            }

            carrelloService.svuotaCarrello(carrello);
            session.setAttribute("successo", "Carrello svuotato con successo!");
            response.sendRedirect("VisualizzaCarrelloServlet");

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Errore durante lo svuotamento del carrello");
        }
    }
}
