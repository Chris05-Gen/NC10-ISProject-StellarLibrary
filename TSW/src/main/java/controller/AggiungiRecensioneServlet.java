package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import model.Recensione;
import model.Utente;
import service.GestioneRecensioniService;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

@WebServlet("/AggiungiRecensioneServlet")
public class AggiungiRecensioneServlet extends HttpServlet {
    private final GestioneRecensioniService recensioniService = new GestioneRecensioniService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Utente utente = (Utente) session.getAttribute("utente");

        if (utente == null) {
            response.sendRedirect("home");
            return;
        }

        Integer idUtente = utente.getId();
        String isbn = request.getParameter("isbn");
        String titolo = request.getParameter("titolo");
        String testo = request.getParameter("testo");
        String valutazioneRaw = request.getParameter("valutazione");

        String errore = null;
        int valutazione = 0;

        // Validazione server-side
        if (isbn == null || isbn.trim().isEmpty()) {
            errore = "ISBN mancante.";
        } else if (titolo == null || titolo.trim().isEmpty()) {
            errore = "Titolo recensione obbligatorio.";
        } else if (testo == null || testo.trim().isEmpty()) {
            errore = "Il testo della recensione è obbligatorio.";
        } else if (testo.length() > 500) {
            errore = "La recensione è troppo lunga (max 500 caratteri).";
        } else if (valutazioneRaw == null) {
            errore = "Valutazione mancante.";
        } else {
            try {
                valutazione = Integer.parseInt(valutazioneRaw);
                if (valutazione < 1 || valutazione > 5) {
                    errore = "Valutazione fuori range (1-5).";
                }
            } catch (NumberFormatException e) {
                errore = "Valutazione non numerica.";
            }
        }

        if (errore != null) {
            request.setAttribute("errore", errore);
            request.setAttribute("isbn", isbn);
            request.getRequestDispatcher("/Interface/dettagliLibro.jsp").forward(request, response);
            return;
        }

        Date data = Date.valueOf(LocalDate.now());
        Recensione recensione = new Recensione(0, idUtente, isbn, titolo, testo, valutazione, data);

        try {
            recensioniService.aggiungiRecensione(recensione);
        } catch (Exception e) {
            throw new ServletException("Errore durante l'aggiunta della recensione", e);
        }

        response.sendRedirect("DettaglioLibroServlet?isbn=" + isbn);
    }
}
