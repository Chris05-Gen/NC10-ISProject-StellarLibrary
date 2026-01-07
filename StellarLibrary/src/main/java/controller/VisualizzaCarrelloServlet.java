package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Utente;
import model.Carrello;
import service.CarrelloService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/VisualizzaCarrelloServlet")
public class VisualizzaCarrelloServlet extends HttpServlet {

    private final CarrelloService carrelloService = new CarrelloService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utente");

        try {
            // 1️⃣ Recupero o creazione carrello (utente o guest)
            Carrello carrello = carrelloService.getOrCreateCarrello(utente, session);

            // 2️⃣ Recupero dettagli + totale
            List<Map<String, Object>> dettagliCarrello = carrelloService.getDettagliCarrello(carrello);
            double totale = carrelloService.getTotaleCarrello(dettagliCarrello);

            // 3️⃣ Passaggio alla JSP
            request.setAttribute("carrelloItems", dettagliCarrello);
            request.setAttribute("totale", totale);

            request.getRequestDispatcher("/Interface/carrello.jsp")
                    .forward(request, response);

        } catch (IllegalArgumentException e) {
            // errori previsti dal contratto → informo l'utente
            session.setAttribute("errore", e.getMessage());
            response.sendRedirect("home");

        } catch (RuntimeException e) {
            // errori imprevisti / DB / service
            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Errore nel caricamento del carrello"
            );
        }
    }
}

