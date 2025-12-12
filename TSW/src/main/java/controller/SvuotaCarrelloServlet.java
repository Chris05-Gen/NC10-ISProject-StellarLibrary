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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utente");

        try {
            // 1️⃣ Recupera/crea il carrello (service gestisce tutto)
            Carrello carrello = carrelloService.getOrCreateCarrello(utente, session);

            // 2️⃣ Delego al service lo svuotamento + validazioni
            carrelloService.svuotaCarrello(carrello);

            // 3️⃣ Successo
            session.setAttribute("successo", "Carrello svuotato con successo!");
            response.sendRedirect("VisualizzaCarrelloServlet");

        } catch (IllegalArgumentException e) {
            // Errori previsti → comunicazione all'utente
            session.setAttribute("errore", e.getMessage());
            response.sendRedirect("VisualizzaCarrelloServlet");

        } catch (RuntimeException e) {
            // Errori tecnici
            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Errore durante lo svuotamento del carrello"
            );
        }
    }
}

