package controller;

import dao.CarrelloDAO;
import dao.ContieneDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/AggiungiAlCarrelloServlet")
public class AggiungiAlCarrelloServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String isbn = request.getParameter("isbn");
        int quantita = Integer.parseInt(request.getParameter("quantita"));

        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utente");

        CarrelloDAO carrelloDAO = new CarrelloDAO();
        ContieneDAO contieneDAO = new ContieneDAO();

        try {
            Carrello carrello;
            Integer carrelloId = null;

            if (utente != null) {
                // Utente loggato: trova o crea il suo carrello
                carrello = carrelloDAO.findByUtenteId(utente.getId());
                if (carrello == null) {
                    carrello = carrelloDAO.createCarrello(utente.getId());
                }
                carrelloId = carrello.getId();
            } else {
                // Utente guest: usa sessione
                Integer guestCarrelloId = (Integer) session.getAttribute("carrelloId");
                if (guestCarrelloId != null) {
                    carrelloId = guestCarrelloId;
                } else {
                    carrello = carrelloDAO.createCarrello(null); // guest ➜ IDUtente = NULL
                    carrelloId = carrello.getId();
                    session.setAttribute("carrelloId", carrelloId);
                }
            }

            // Recupera contenuto attuale del libro (se presente)
            List<Contiene> contenuti = contieneDAO.getContenuto(carrelloId);
            int nuovaQuantita = quantita;

            for (Contiene c : contenuti) {
                if (c.getIsbn().equals(isbn)) {
                    nuovaQuantita += c.getQuantita();
                    break;
                }
            }

            // Aggiungi o aggiorna libro
            contieneDAO.aggiungiLibro(carrelloId, isbn, nuovaQuantita);

            // Redireziona alla pagina precedente o al carrello
            response.sendRedirect("VisualizzaCarrelloServlet");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore nell'aggiunta al carrello");
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
