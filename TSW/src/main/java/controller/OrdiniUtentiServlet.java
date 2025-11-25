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
import java.util.List;
import java.util.Map;



@WebServlet("/OrdiniUtentiServlet")
public class OrdiniUtentiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utente admin = (Utente) request.getSession().getAttribute("utente");

        // se l'utente non è un admin viene rimbalzato alla home
        if (admin == null || !"Admin".equals(admin.getTipo())) {
            session.setAttribute("errore", "Indirizzo non valido.");
            response.sendRedirect("home");
            return;
        }
        OrdineDAO ordineDAO = new OrdineDAO();
        MetodoPagamentoDAO pagamentoDAO = new MetodoPagamentoDAO();
        IndirizzoDAO indirizzoDAO = new IndirizzoDAO();
        // recupero tutte le informazioni sull'ordine
        try {
            List<Map<String, Object>> ordini = ordineDAO.getOrdiniConUtente();
            // recupero le corrispondenze id corrispettivo valore nel DB
            for (Map<String, Object> ordine : ordini) {
                int idMetodo = (int) ordine.get("idMetodoPagamento");
                int idIndirizzo = (int) ordine.get("idIndirizzo");

                MetodoPagamento mp = pagamentoDAO.getMetodoById(idMetodo);
                Indirizzo indirizzo = indirizzoDAO.getIndirizzoById(idIndirizzo);

                ordine.put("metodoPagamento", mp);
                ordine.put("indirizzo", indirizzo);
            }
            // invio i dati alla jsp tramite la richiesta
            request.setAttribute("ordiniUtenti", ordini);
            request.getRequestDispatcher("/Interface/ordiniUtenti.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("/Interface/errore.jsp");
        }
    }
}
