package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Indirizzo;
import dao.IndirizzoDAO;
import model.Utente;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/AggiungiIndirizzoServlet")
public class AggiungiIndirizzoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utente");

        //Metodo di sicurezza, se per sbaglio un utente guest
        // accede alla servlet viene rimabalzato a home
        if (utente == null) {
            response.sendRedirect("home");
            return;
        }

        //recupero tutti i dati dal form, per inizializzare un oggetto indirizzo
        Indirizzo nuovo = new Indirizzo();
        nuovo.setUtente(utente);
        nuovo.setVia(request.getParameter("via"));
        nuovo.setCitta(request.getParameter("citta"));
        nuovo.setCap(request.getParameter("cap"));
        nuovo.setProvincia(request.getParameter("provincia"));
        nuovo.setNazione(request.getParameter("nazione"));
        nuovo.setTelefono(request.getParameter("telefono"));

        IndirizzoDAO dao = new IndirizzoDAO();

        //aggiungo l'indirizzo al DB
        try {
            dao.addIndirizzo(nuovo);
            session.setAttribute("successo", "Indirizzo aggiunto con successo!");
            response.sendRedirect("CheckoutServlet");
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errore", "Errore durante il salvataggio dell'indirizzo.");
            response.sendRedirect("CheckoutServlet");
        }
    }
}
