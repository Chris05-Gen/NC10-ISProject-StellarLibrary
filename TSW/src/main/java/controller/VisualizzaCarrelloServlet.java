package controller;

import dao.CarrelloDAO;
import dao.ContieneDAO;
import dao.LibroDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/VisualizzaCarrelloServlet")
public class VisualizzaCarrelloServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        CarrelloDAO carrelloDAO = new CarrelloDAO();
        ContieneDAO contieneDAO = new ContieneDAO();
        LibroDAO libroDAO = new LibroDAO(); // Presunto esistente

        Carrello carrello = null;
        Integer carrelloId = (Integer) session.getAttribute("carrelloId");

        try {
            Utente utente = (Utente) session.getAttribute("utente");

            // Utente loggato
            if (utente != null) {
                carrello = carrelloDAO.findByUtenteId(utente.getId());
                if (carrello == null) {
                    carrello = carrelloDAO.createCarrello(utente.getId());
                }
            }
            // Guest
            else {
                if (carrelloId != null) {
                    carrello = new Carrello();
                    carrello.setId(carrelloId);
                    carrello.setIdUtente(-1);
                } else {
                    carrello = carrelloDAO.createCarrello(null);
                    session.setAttribute("carrelloId", carrello.getId());
                }
            }

            List<Contiene> contenuti = contieneDAO.getContenuto(carrello.getId());
            List<Map<String, Object>> dettagliCarrello = new ArrayList<>();

            BigDecimal totale = BigDecimal.ZERO;
            for (Contiene c : contenuti) {
                Libro libro = libroDAO.findByISBN(c.getIsbn());

                BigDecimal subTotaleBD = libro.getPrezzo().multiply(BigDecimal.valueOf(c.getQuantita()));
                double subTotale = subTotaleBD.doubleValue();
                totale = totale.add(subTotaleBD);

                Map<String, Object> item = new HashMap<>();
                item.put("libro", libro);
                item.put("quantita", c.getQuantita());
                item.put("subTotale", subTotale);

                dettagliCarrello.add(item);
            }

            request.setAttribute("carrelloItems", dettagliCarrello);
            request.setAttribute("totale", totale.doubleValue());


            request.setAttribute("carrelloItems", dettagliCarrello);
            request.setAttribute("totale", totale);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/Interface/carrello.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore nel caricamento del carrello");
        }
    }
}
