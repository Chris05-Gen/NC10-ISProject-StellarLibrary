package controller;

import dao.IndirizzoDAO;
import dao.MetodoPagamentoDAO;
import dao.OrdineDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.*;
import service.GestioneOrdiniService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;



@WebServlet("/OrdiniUtentiServlet")
public class OrdiniUtentiServlet extends HttpServlet {

    private final GestioneOrdiniService ordiniService = new GestioneOrdiniService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Utente admin = (Utente) session.getAttribute("utente");

        // 1️⃣ Controllo autorizzazione
        if (admin == null || !"Admin".equals(admin.getTipo())) {
            session.setAttribute("errore", "Account non autorizzato.");
            response.sendRedirect("home");
            return;
        }

        try {
            // 2️⃣ Recupero tramite service
            List<Ordine> ordini = ordiniService.getOrdiniConUtente();

            request.setAttribute("ordiniUtenti", ordini);
            request.getRequestDispatcher("/Interface/ordiniUtenti.jsp")
                    .forward(request, response);

        } catch (IllegalStateException e) {
            // Violazioni dei contratti lato DAO
            session.setAttribute("errore", "Dati degli ordini non coerenti.");
            response.sendRedirect("home");

        } catch (RuntimeException e) {
            // Errori generici
            session.setAttribute("errore", "Errore nel caricamento degli ordini utenti.");
            response.sendRedirect("home");
        }
    }

}

