package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import service.GestioneOrdiniService;
import model.Utente;

import java.io.IOException;

@WebServlet("/CreaOrdineServlet")
public class CreaOrdineServlet extends HttpServlet {

    private final GestioneOrdiniService ordiniService = new GestioneOrdiniService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Utente u = (Utente) session.getAttribute("utente");

        if (u == null) {
            response.sendRedirect("home");
            return;
        }

        String indirizzoRaw = request.getParameter("idIndirizzo");
        String metodoRaw = request.getParameter("metodoPagamento");

        try {
            int idOrdine = ordiniService.creaOrdineCompleto(
                    u.getId(),
                    indirizzoRaw,
                    metodoRaw
            );

            session.setAttribute("successo", "Acquisto effettuato! Ordine #" + idOrdine);
            response.sendRedirect("home");

        } catch (IllegalArgumentException e) {
            // errore di validazione input RAW
            session.setAttribute("errore", e.getMessage());
            response.sendRedirect("CheckoutServlet");

        } catch (IllegalStateException e) {
            // errore di business logic (indirizzo non tuo, carrello vuoto ...)
            session.setAttribute("errore", e.getMessage());
            response.sendRedirect("CheckoutServlet");

        } catch (Exception e) {
            session.setAttribute("errore", "Errore inatteso durante la creazione dell'ordine.");
            response.sendRedirect("home");
        }

    }
}
