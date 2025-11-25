package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Recensione;
import dao.RecensioneDAO;
import model.Utente;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/GestioneRecensioniServlet")
public class GestioneRecensioniServlet extends HttpServlet {
    private final RecensioneDAO recensioneDAO = new RecensioneDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utente admin = (Utente) request.getSession().getAttribute("utente");
        // recupero attributo utente dalla sessione, se non admin rimbalzo a home
        if (admin == null || !"Admin".equals(admin.getTipo())) {
            session.setAttribute("errore","Account non autorizzato");
            response.sendRedirect("home");
            return;
        }
        // recupero tutt le info delle recensioni fatte da tutte gli utenti
        try {
            List<Recensione> recensioni = recensioneDAO.getAllRecensioniConNomeUtente();
            request.setAttribute("recensioni", recensioni);
            request.getRequestDispatcher("/Interface/gestioneRecensioni.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errore","Errore nell'accesso alle recensioni");
            response.sendRedirect("home");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // nella pagina che visualizza
        // tutte le recensioni vi è la possibilità di eliminarle tramite un bottone in un form POST
        HttpSession session = request.getSession();
        int id = Integer.parseInt(request.getParameter("idRecensione"));

        // recupero id della recensione dal form e la elimino, infine ricarico
        // la pagina normalmente in modo da vedere direttamente l'eliminazione del commento
        try {
            recensioneDAO.deleteRecensioneById(id);
        } catch (SQLException e) {
            session.setAttribute("errore","Errore nell'eliminazione della recensione");
            e.printStackTrace();
        }
        response.sendRedirect("GestioneRecensioniServlet");
    }
}
