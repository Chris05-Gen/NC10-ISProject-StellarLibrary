package controller;

import dao.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utente");

        // Misura di Sicurezza nel caso Guest acceda alla Servlet
        if (utente == null) {
            response.sendRedirect("home");
            return;
        }

        
        CarrelloDAO carrelloDAO = new CarrelloDAO();
        ContieneDAO contieneDAO = new ContieneDAO();
        LibroDAO libroDAO = new LibroDAO();
        IndirizzoDAO indirizzoDAO = new IndirizzoDAO();
        MetodoPagamentoDAO metodoPagamentoDAO = new MetodoPagamentoDAO();

        try {
            Carrello carrello = carrelloDAO.findByUtenteId(utente.getId());
            if (carrello == null) {
                carrello = carrelloDAO.createCarrello(utente.getId()); // crea uno nuovo se assente
            }

            // Recupero la lista di tutti i libri nel carrello
            List<Contiene> contenuti = contieneDAO.getContenuto(carrello.getId());


            List<Map<String, Object>> dettagliCarrello = new ArrayList<>();
            BigDecimal totale = BigDecimal.ZERO;
            // Per ogni libro del carrello preparo 3 coppie Stringa-Oggetto 
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
            // Recupero tutti gli indirizzi dell'Utente
            List<Indirizzo> indirizzi = indirizzoDAO.getIndirizziByUtente(utente.getId());
            
            // Recupero tutti i metodiPagamento dal DB
            List<MetodoPagamento> metodiPagamento = metodoPagamentoDAO.getAll();
            
            //Passo tutto alla jsp
            request.setAttribute("metodiPagamento", metodiPagamento);
            request.setAttribute("carrelloItems", dettagliCarrello);
            request.setAttribute("totale", totale.doubleValue());
            request.setAttribute("indirizzi", indirizzi);

            request.getRequestDispatcher("/Interface/checkout.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore durante il caricamento del checkout");
        }
    }
}
