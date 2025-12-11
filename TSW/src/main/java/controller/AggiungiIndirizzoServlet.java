package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Utente;
import service.GestioneIndirizziService;

import java.io.IOException;

@WebServlet("/AggiungiIndirizzoServlet")
public class AggiungiIndirizzoServlet extends HttpServlet {

    private final GestioneIndirizziService indirizziService = new GestioneIndirizziService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utente");

        // controllo autenticazione
        if (utente == null) {
            response.sendRedirect("home");
            return;
        }

        try {
            indirizziService.creaIndirizzoPerUtente(
                    utente.getId(),
                    request.getParameter("via"),
                    request.getParameter("citta"),
                    request.getParameter("cap"),
                    request.getParameter("provincia"),
                    request.getParameter("nazione"),
                    request.getParameter("telefono")
            );

            session.setAttribute("successo", "Indirizzo aggiunto con successo!");
            response.sendRedirect("CheckoutServlet");

        } catch (IllegalArgumentException e) {
            // errore di validazione dei dati
            session.setAttribute("errore", e.getMessage());
            response.sendRedirect("CheckoutServlet");

        } catch (Exception e) {
            // errore interno service o DAO
            session.setAttribute("errore", "Errore durante il salvataggio dell'indirizzo.");
            response.sendRedirect("CheckoutServlet");
        }
    }
}
