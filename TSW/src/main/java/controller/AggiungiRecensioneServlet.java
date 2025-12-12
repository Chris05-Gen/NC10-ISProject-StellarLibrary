package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import model.Utente;
import service.GestioneRecensioniService;

import java.io.IOException;

@WebServlet("/AggiungiRecensioneServlet")
public class AggiungiRecensioneServlet extends HttpServlet {

    private final GestioneRecensioniService recensioniService = new GestioneRecensioniService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Utente utente = (session != null) ? (Utente) session.getAttribute("utente") : null;

        if (utente == null) {
            response.sendRedirect("home");
            return;
        }

        // Parametri grezzi
        String isbn = request.getParameter("isbn");
        String titolo = request.getParameter("titolo");
        String testo = request.getParameter("testo");
        String valutazioneRaw = request.getParameter("valutazione");

        try {
            // VALIDAZIONE delegata al service
            recensioniService.validaRecensione(isbn, titolo, testo, valutazioneRaw);

            // Conversione valutazione dopo validazione
            int valutazione = Integer.parseInt(valutazioneRaw);

            // CREAZIONE recensione tramite service
            recensioniService.aggiungiRecensione(utente, isbn, titolo, testo, valutazione);

            // Redirect al dettaglio libro
            response.sendRedirect("DettaglioLibroServlet?isbn=" + isbn);

        } catch (IllegalArgumentException e) {
            request.setAttribute("errore", e.getMessage());

            // Mantieni ISBN per ricaricare il dettaglio
            response.sendRedirect("DettaglioLibroServlet?isbn=" + isbn);
        }
        catch (Exception e) {
            throw new ServletException("Errore durante l'aggiunta della recensione", e);
        }
    }
}
