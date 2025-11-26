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

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utente");
        try {
            Carrello carrello = carrelloService.getOrCreateCarrello(utente, session);
            List<Map<String, Object>> dettagliCarrello = carrelloService.getDettagliCarrello(carrello);
            double totale = carrelloService.getTotaleCarrello(dettagliCarrello);

            request.setAttribute("carrelloItems", dettagliCarrello);
            request.setAttribute("totale", totale);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/Interface/carrello.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore nel caricamento del carrello");
        }
    }
}
