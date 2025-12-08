package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Indirizzo;
import model.MetodoPagamento;
import model.Ordine;
import model.Utente;
import service.GestioneOrdiniService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/VisualizzaOrdiniServlet")
public class VisualizzaOrdiniServlet extends HttpServlet {
    private final GestioneOrdiniService ordiniService = new GestioneOrdiniService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Utente u = (Utente) session.getAttribute("utente");

        if (u == null) {
            session.setAttribute("errore", "Utente inesistente");
            response.sendRedirect("home");
            return;
        }

        try {
            List<Ordine> ordini = ordiniService.getOrdiniByUtente(u.getId());
            Map<Integer, MetodoPagamento> mappaPagamenti = ordiniService.getMetodiPagamentoPerOrdini(ordini);
            Map<Integer, Indirizzo> mappaIndirizzi = ordiniService.getIndirizziPerOrdini(ordini);

            request.setAttribute("ordini", ordini);
            request.setAttribute("mappaPagamenti", mappaPagamenti);
            request.setAttribute("mappaIndirizzi", mappaIndirizzi);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/Interface/ordini.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            response.sendError(500, "Errore durante il recupero degli ordini");
        }
    }
}
