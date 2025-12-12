package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Carrello;
import model.Utente;
import service.CarrelloService;

import java.io.IOException;

@WebServlet("/AggiungiAlCarrelloServlet")
public class AggiungiAlCarrelloServlet extends HttpServlet {

    private final CarrelloService carrelloService = new CarrelloService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utente");

        String isbn = request.getParameter("isbn");
        String quantitaRaw = request.getParameter("quantita");

        try {
            int quantita = Integer.parseInt(quantitaRaw);

            Carrello carrello = carrelloService.getOrCreateCarrello(utente, session);
            carrelloService.aggiungiLibroAlCarrello(carrello, isbn, quantita);

            response.sendRedirect("VisualizzaCarrelloServlet");

        } catch (NumberFormatException e) {
            session.setAttribute("errore", "Quantità non valida.");
            response.sendRedirect("home");

        } catch (IllegalArgumentException | IllegalStateException e) {
            session.setAttribute("errore", e.getMessage());
            response.sendRedirect("home");

        } catch (RuntimeException e) {
            session.setAttribute("errore", "Errore nell'aggiunta al carrello.");
            response.sendRedirect("home");
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

}

