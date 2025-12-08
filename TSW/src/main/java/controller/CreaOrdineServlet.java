package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Carrello;
import model.Contiene;
import model.Utente;
import service.GestioneOrdiniService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/CreaOrdineServlet")
public class CreaOrdineServlet extends HttpServlet {
    private final GestioneOrdiniService ordiniService = new GestioneOrdiniService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Utente u = (Utente) session.getAttribute("utente");
        if (u == null) {
            response.sendRedirect("home");
            return;
        }

        String indirizzoRaw = request.getParameter("idIndirizzo");
        String metodoRaw = request.getParameter("metodoPagamento");

        if (indirizzoRaw == null || metodoRaw == null) {
            session.setAttribute("errore", "Seleziona indirizzo e metodo di pagamento.");
            response.sendRedirect("CheckoutServlet");
            return;
        }

        int idIndirizzo;
        int idMetodo;
        try {
            idIndirizzo = Integer.parseInt(indirizzoRaw);
            idMetodo = Integer.parseInt(metodoRaw);
        } catch (NumberFormatException e) {
            session.setAttribute("errore", "Valori non validi per indirizzo o metodo di pagamento.");
            response.sendRedirect("CheckoutServlet");
            return;
        }

        try {
            if (!ordiniService.indirizzoAppartieneAUtente(idIndirizzo, u.getId())) {
                session.setAttribute("errore", "Indirizzo non valido.");
                response.sendRedirect("CheckoutServlet");
                return;
            }

            if (!ordiniService.metodoPagamentoEsiste(idMetodo)) {
                session.setAttribute("errore", "Metodo di pagamento non valido.");
                response.sendRedirect("CheckoutServlet");
                return;
            }

            Carrello carrello = ordiniService.getCarrelloByUtente(u.getId());
            List<Contiene> items = ordiniService.getContenutoCarrello(carrello.getId());

            if (items.isEmpty()) {
                session.setAttribute("errore", "Carrello vuoto.");
                response.sendRedirect("VisualizzaCarrelloServlet");
                return;
            }

            BigDecimal totale = ordiniService.calcolaTotaleOrdine(items);
            int nuovoId = ordiniService.creaOrdine(u.getId(), idIndirizzo, idMetodo, totale);
            ordiniService.svuotaCarrello(carrello.getId());

            session.setAttribute("successo", "Acquisto effettuato! Ordine #" + nuovoId);
            response.sendRedirect("home");

        } catch (Exception e) {
            session.setAttribute("errore", "Errore DB indirizzo");
            response.sendRedirect("home");
        }
    }
}
