package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Carrello;
import model.Indirizzo;
import model.MetodoPagamento;
import model.Utente;
import service.CarrelloService;
import service.GestioneOrdiniService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {
    private final CarrelloService carrelloService = new CarrelloService();
    private final GestioneOrdiniService ordiniService = new GestioneOrdiniService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utente");

        if (utente == null) {
            response.sendRedirect("home");
            return;
        }

        try {
            // carrello (riuso CarrelloService)
            Carrello carrello = carrelloService.getOrCreateCarrello(utente, session);
            List<Map<String, Object>> dettagliCarrello = carrelloService.getDettagliCarrello(carrello);
            double totale = carrelloService.getTotaleCarrello(dettagliCarrello);

            // indirizzi e metodi di pagamento (GestioneOrdiniService)
            List<Indirizzo> indirizzi = ordiniService.getIndirizziByUtente(utente.getId());
            List<MetodoPagamento> metodiPagamento = ordiniService.getTuttiIMetodiPagamento();

            request.setAttribute("metodiPagamento", metodiPagamento);
            request.setAttribute("carrelloItems", dettagliCarrello);
            request.setAttribute("totale", totale);
            request.setAttribute("indirizzi", indirizzi);

            request.getRequestDispatcher("/Interface/checkout.jsp").forward(request, response);

        } catch (IllegalArgumentException e) {
            session.setAttribute("errore", e.getMessage());
            response.sendRedirect("home");

        } catch (RuntimeException e) {
            session.setAttribute("errore", "Errore durante il caricamento del checkout.");
            response.sendRedirect("home");
        }

    }
}
