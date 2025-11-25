package controller;

import dao.IndirizzoDAO;
import dao.MetodoPagamentoDAO;
import dao.OrdineDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@WebServlet("/VisualizzaOrdiniServlet")
public class VisualizzaOrdiniServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utente u = (Utente) session.getAttribute("utente");

        // se un utente guest riesce a invocare questa pagina lo rimbalziamo alla home
        if (u == null) {
            session.setAttribute("errore", "Utente inesistente");
            response.sendRedirect("home");
            return;
        }

        int idUtente = u.getId();
        OrdineDAO ordineDAO = new OrdineDAO();
        MetodoPagamentoDAO pagamentoDAO = new MetodoPagamentoDAO();
        IndirizzoDAO indirizzoDAO = new IndirizzoDAO();

        try {
            List<Ordine> ordini = ordineDAO.getOrdiniByUtente(idUtente);

            // Mappa Ordine ID → MetodoPagamento e Indirizzo, 
            // per ogni ordine recupero dall'id del pagamento e dell'indirizzo 
            // a cosa corrispondono nelle rispettiva tabelle
            Map<Integer, MetodoPagamento> mappaPagamenti = new HashMap<>();
            Map<Integer, Indirizzo> mappaIndirizzi = new HashMap<>();

            // recuper e associo a ogni ordine metodo e indirizzo corrispondenti
            for (Ordine ordine : ordini) {
                MetodoPagamento pagamento = pagamentoDAO.getMetodoById(ordine.getIdMetodoPagamento());
                Indirizzo indirizzo = indirizzoDAO.getIndirizzoById(ordine.getIdIndirizzo());

                mappaPagamenti.put(ordine.getId(), pagamento);
                mappaIndirizzi.put(ordine.getId(), indirizzo);
            }
            
            // li invio alla pagina ordini.jsp tramite la richiesta
            request.setAttribute("ordini", ordini);
            request.setAttribute("mappaPagamenti", mappaPagamenti);
            request.setAttribute("mappaIndirizzi", mappaIndirizzi);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/Interface/ordini.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(500, "Errore durante il recupero degli ordini");
        }
    }
}

