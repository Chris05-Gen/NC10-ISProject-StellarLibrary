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

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String errore = null;

        if (email == null || !email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            errore = "Email non valida.";
        } else if (password == null || password.trim().isEmpty()) {
            errore = "La password è obbligatoria.";
        }

        if (errore != null) {
            request.setAttribute("errore", errore);
            request.getRequestDispatcher("home").forward(request, response);
            return;
        }

        try {
            Utente utente = autenticazioneService.login(email, password);

            if (utente != null) {
                HttpSession session = request.getSession();
                session.setAttribute("utente", utente);

                Integer guestCarrelloId = (Integer) session.getAttribute("carrelloId");
                carrelloService.unisciCarrelloGuestConUtente(guestCarrelloId, utente);
                session.removeAttribute("carrelloId");

                session.setAttribute("successo", "Login effettuato con successo!");
                response.sendRedirect("home");
            } else {
                HttpSession session = request.getSession();
                session.setAttribute("erroreLogin", "Credenziali non valide.");
                response.sendRedirect("home");
            }

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore durante il login.");
        }
    }
}
