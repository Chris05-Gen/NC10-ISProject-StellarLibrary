package controller;

import dao.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@WebServlet("/CreaOrdineServlet")
public class CreaOrdineServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utente u = (Utente) session.getAttribute("utente");
        // se utente guest vi ci accede, viene rimbalzato alla home
        if (u == null) {
            response.sendRedirect("home");
            return;
        }
        //prendo dati dal form
        String indirizzoRaw = request.getParameter("idIndirizzo");
        String metodoRaw = request.getParameter("metodoPagamento");

        //valido i dati ricevuti a livello di valori
        if (indirizzoRaw == null || metodoRaw == null) {
            session.setAttribute("errore", "Seleziona indirizzo e metodo di pagamento.");
            response.sendRedirect("CheckoutServlet");
            return;
        }

        int idIndirizzo;
        int idMetodo;
        //vedo se i valori sono validi e pertanto presenti nel DB
        try {
            idIndirizzo = Integer.parseInt(indirizzoRaw);
            idMetodo = Integer.parseInt(metodoRaw);
        } catch (NumberFormatException e) {
            session.setAttribute("errore", "Valori non validi per indirizzo o metodo di pagamento.");
            response.sendRedirect("CheckoutServlet");
            return;
        }
        //verifico che l'indirizzo oltre che a esistere, appartenga
        // all'utente che ne fa richeista
        try {
            IndirizzoDAO indirizzoDAO = new IndirizzoDAO();
            if (!indirizzoDAO.AppartieneA(idIndirizzo, u.getId())) {
                session.setAttribute("errore", "Indirizzo non valido.");
                response.sendRedirect("CheckoutServlet");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore DB indirizzo");
            return;
        }
        //verifica che metodo di PAgamento esista nel DB
        try {
            MetodoPagamentoDAO mpDAO = new MetodoPagamentoDAO();
            if (!mpDAO.Esiste(idMetodo)) {
                session.setAttribute("errore", "Metodo di pagamento non valido.");
                response.sendRedirect("CheckoutServlet");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errore", "Errore DB indirizzo");
            response.sendRedirect("CheckoutServlet");
            return;
        }


        try {
            int idUtente = u.getId();
            // Recupero il carrello dell'utente grazie al suo id
            // , e con esso prendo tutti gli elementi che esso contiene
            Carrello carrello = new CarrelloDAO().findByUtenteId(idUtente);
            List<Contiene> items = new ContieneDAO().getContenuto(carrello.getId());

            if (items.isEmpty()) {
                session.setAttribute("errore", "Carrello vuoto.");
                response.sendRedirect("VisualizzaCarrelloServlet");
                return;
            }

            //calcolo il totale del carrello
            BigDecimal totale = BigDecimal.ZERO;
            for (Contiene c : items) {
                BigDecimal prezzo = new LibroDAO().getPrezzoByIsbn(c.getIsbn());
                BigDecimal sub = prezzo.multiply(BigDecimal.valueOf(c.getQuantita()));
                totale = totale.add(sub);
            }
            // memorizzo l'oggetto ordine nel DB con un DAO
            Ordine ordine = new Ordine(0, idUtente, idIndirizzo, idMetodo,
                    new Timestamp(System.currentTimeMillis()), totale);
            int nuovoId = new OrdineDAO().creaOrdine(ordine);
            // svuoto infine il carrello appena comperato
            new ContieneDAO().svuotaCarrello(carrello.getId());

            session.setAttribute("successo", "Acquisto effettuato! Ordine #" + nuovoId);
            response.sendRedirect("home");

        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errore", "Errore DB indirizzo");
            response.sendRedirect("home");
            return;
        }

    }
}


