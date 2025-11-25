package controller;

import dao.CarrelloDAO;
import dao.ContieneDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/SvuotaCarrelloServlet")
public class SvuotaCarrelloServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        CarrelloDAO carrelloDAO = new CarrelloDAO();
        ContieneDAO contieneDAO = new ContieneDAO();

        try {
            Utente utente = (Utente) session.getAttribute("utente");
            int carrelloId;
            // Utente Loggato : recupero carrello con idUtente dal DB
            if (utente != null) {
                Carrello carrello = carrelloDAO.findByUtenteId(utente.getId());
                carrelloId = carrello.getId();
            } // Utente GUest : recupero il carrello con carrelloId salvato in sessione
            else {
                Integer id = (Integer) session.getAttribute("carrelloId");
                if (id == null) {
                    response.sendRedirect("VisualizzaCarrelloServlet");
                    return;
                }
                carrelloId = id;
            }

            contieneDAO.svuotaCarrello(carrelloId);
            session.setAttribute("successo", "Carrello svuotato con successo!");
            response.sendRedirect("VisualizzaCarrelloServlet");     

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore durante lo svuotamento del carrello");
        }
    }
}
