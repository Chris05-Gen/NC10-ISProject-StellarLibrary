package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Utente;
import model.Ordine;
import service.GestioneOrdiniService;

import java.io.IOException;
import java.util.List;

@WebServlet("/OrdiniUtentiServlet")
public class OrdiniUtentiServlet extends HttpServlet {

    private final GestioneOrdiniService ordiniService = new GestioneOrdiniService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Utente admin = (session != null) ? (Utente) session.getAttribute("utente") : null;

        // 1️⃣ Precondizione: deve essere admin
        if (admin == null || !"Admin".equals(admin.getTipo())) {
            if (session != null)
                session.setAttribute("errore", "Accesso non autorizzato.");
            response.sendRedirect("home");
            return;
        }

        try {
            // 2️⃣ Business logic completamente nel service
            List<Ordine> ordini = ordiniService.getOrdiniCompletiPerAdmin();

            // 3️⃣ Invio alla JSP
            request.setAttribute("ordiniUtenti", ordini);
            request.getRequestDispatcher("/Interface/ordiniUtenti.jsp")
                    .forward(request, response);

        } catch (IllegalArgumentException e) {
            // Errori previsti → messaggio all'utente
            session.setAttribute("errore", e.getMessage());
            response.sendRedirect("home");

        } catch (IllegalStateException e) {
            // Errori di coerenza dei dati
            session.setAttribute("errore", "Dati incoerenti negli ordini.");
            response.sendRedirect("home");

        } catch (RuntimeException e) {
            // Errori DAO / di sistema
            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Errore nel caricamento degli ordini utenti."
            );
        }
    }
}
