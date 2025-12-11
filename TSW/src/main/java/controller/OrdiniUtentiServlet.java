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

        // 1️⃣ controllo autorizzazione admin
        if (admin == null || !"Admin".equals(admin.getTipo())) {
            session.setAttribute("errore", "Accesso non autorizzato.");
            response.sendRedirect("home");
            return;
        }

        try {
            // 2️⃣ uso del service (nessun DAO nel controller!)
            List<Ordine> ordini = ordiniService.getOrdiniCompletiPerAdmin();

            // 3️⃣ invio alla JSP
            request.setAttribute("ordiniUtenti", ordini);
            request.getRequestDispatcher("/Interface/ordiniUtenti.jsp")
                    .forward(request, response);

        } catch (IllegalArgumentException e) {
            session.setAttribute("errore", e.getMessage());
            response.sendRedirect("home");
        } catch (IllegalStateException e) {
            session.setAttribute("errore", "Dati incoerenti negli ordini.");
            response.sendRedirect("home");
        } catch (RuntimeException e) {
            session.setAttribute("errore", "Errore nel caricamento degli ordini utenti.");
            response.sendRedirect("home");
        }
    }

}

