package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Recensione;
import model.Utente;
import service.GestioneRecensioniService;

import java.io.IOException;
import java.util.List;

@WebServlet("/GestioneRecensioniServlet")
public class GestioneRecensioniServlet extends HttpServlet {
    private final GestioneRecensioniService recensioniService = new GestioneRecensioniService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utente admin = (Utente) session.getAttribute("utente");
        if (admin == null || !"Admin".equals(admin.getTipo())) {
            session.setAttribute("errore", "Account non autorizzato");
            response.sendRedirect("home");
            return;
        }
        try {
            List<Recensione> recensioni = recensioniService.getAllRecensioniConNomeUtente();
            request.setAttribute("recensioni", recensioni);
            request.getRequestDispatcher("/Interface/gestioneRecensioni.jsp").forward(request, response);
        } catch (Exception e) {
            session.setAttribute("errore", "Errore nell'accesso alle recensioni");
            response.sendRedirect("home");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        int id = Integer.parseInt(request.getParameter("idRecensione"));
        try {
            recensioniService.eliminaRecensione(id);
        } catch (Exception e) {
            session.setAttribute("errore", "Errore nell'eliminazione della recensione");
        }
        response.sendRedirect("GestioneRecensioniServlet");
    }
}
