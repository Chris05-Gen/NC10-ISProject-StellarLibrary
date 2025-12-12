package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Ordine;
import model.Utente;
import service.GestioneOrdiniService;

import java.io.IOException;
import java.util.List;

@WebServlet("/VisualizzaOrdiniServlet")
public class VisualizzaOrdiniServlet extends HttpServlet {

    private final GestioneOrdiniService ordiniService = new GestioneOrdiniService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Utente u = (Utente) session.getAttribute("utente");

        // 1️⃣ Validazione sessione (solo controllo sessione → NON nel service)
        if (u == null) {
            session.setAttribute("errore", "Devi effettuare il login per visualizzare i tuoi ordini.");
            response.sendRedirect("home");
            return;
        }

        try {
            // 2️⃣ Recupero ordini tramite service (service fa tutto)
            List<Ordine> ordini = ordiniService.getOrdiniByUtente(u.getId());

            // 3️⃣ Passaggio alla JSP
            request.setAttribute("ordini", ordini);
            request.getRequestDispatcher("/Interface/ordini.jsp")
                    .forward(request, response);

        } catch (IllegalArgumentException e) {
            session.setAttribute("errore", e.getMessage());
            response.sendRedirect("home");

        } catch (RuntimeException e) {
            session.setAttribute("errore", "Errore durante il recupero dei tuoi ordini.");
            response.sendRedirect("home");
        }
    }
}
