package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Utente;
import service.AutenticazioneService;
import service.CarrelloService;

import java.io.IOException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private final AutenticazioneService autenticazioneService = new AutenticazioneService();
    private final CarrelloService carrelloService = new CarrelloService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            Utente utente = autenticazioneService.login(email, password);

            if (utente == null) {
                HttpSession session = request.getSession();
                session.setAttribute("erroreLogin", "Credenziali non valide.");
                response.sendRedirect("home");
                return;
            }

            HttpSession session = request.getSession();
            session.setAttribute("utente", utente);

            // 1️⃣ Merge PRIMA di qualunque getOrCreate
            Integer guestCarrelloId = (Integer) session.getAttribute("carrelloId");
            if (guestCarrelloId != null) {
                carrelloService.unisciCarrelloGuestConUtente(guestCarrelloId, utente);
                session.removeAttribute("carrelloId");
            }

            // 2️⃣ Successo
            session.setAttribute("successo", "Login effettuato con successo!");
            response.sendRedirect("home");

        } catch (IllegalArgumentException e) {
            request.setAttribute("errore", e.getMessage());
            request.getRequestDispatcher("home").forward(request, response);

        } catch (RuntimeException e) {
            request.setAttribute("errore", "Errore durante il login.");
            request.getRequestDispatcher("home").forward(request, response);
        }
    }
}


