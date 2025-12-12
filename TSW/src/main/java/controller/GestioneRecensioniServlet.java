package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Utente;
import service.GestioneRecensioniService;

import java.io.IOException;

@WebServlet("/GestioneRecensioniServlet")
public class GestioneRecensioniServlet extends HttpServlet {

    private final GestioneRecensioniService recensioniService = new GestioneRecensioniService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Utente admin = (Utente) session.getAttribute("utente");

        // 🔒 1) controllo permessi
        if (admin == null || !"Admin".equals(admin.getTipo())) {
            session.setAttribute("errore", "Account non autorizzato");
            response.sendRedirect("home");
            return;
        }

        try {
            // 2) tutti i dati arrivano già validati/confezionati dal service
            request.setAttribute("recensioni",
                    recensioniService.getAllRecensioniConNomeUtente());

            request.getRequestDispatcher("/Interface/gestioneRecensioni.jsp")
                    .forward(request, response);

        } catch (RuntimeException e) {
            session.setAttribute("errore", "Errore nell'accesso alle recensioni");
            response.sendRedirect("home");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Utente admin = (Utente) session.getAttribute("utente");

        if (admin == null || !"Admin".equals(admin.getTipo())) {
            session.setAttribute("errore", "Account non autorizzato");
            response.sendRedirect("home");
            return;
        }

        String idRaw = request.getParameter("idRecensione");

        try {
            // 👉 delega totale al service: conversione, validazione e delete
            int id = Integer.parseInt(idRaw);  // Alternativa: spostarlo nel service

            recensioniService.eliminaRecensione(id);

            session.setAttribute("successo", "Recensione eliminata con successo!");
            response.sendRedirect("GestioneRecensioniServlet");

        } catch (NumberFormatException e) {
            session.setAttribute("errore", "ID recensione non valido.");
            response.sendRedirect("GestioneRecensioniServlet");

        } catch (RuntimeException e) {
            session.setAttribute("errore", "Errore nell'eliminazione della recensione.");
            response.sendRedirect("GestioneRecensioniServlet");
        }
    }
}
