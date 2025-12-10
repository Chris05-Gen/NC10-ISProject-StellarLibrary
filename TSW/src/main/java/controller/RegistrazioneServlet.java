package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Utente;
import service.RegistrazioneService;

import java.io.IOException;

@WebServlet("/RegistrazioneServlet")
public class RegistrazioneServlet extends HttpServlet {
    private final RegistrazioneService registrazioneService = new RegistrazioneService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String errore = null;

        if (nome == null || nome.trim().isEmpty()) {
            errore = "Il nome è obbligatorio.";
        } else if (cognome == null || cognome.trim().isEmpty()) {
            errore = "Il cognome è obbligatorio.";
        } else if (email == null || !email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            errore = "Email non valida.";
        } else if (password == null || password.length() < 6) {
            errore = "La password deve essere di almeno 6 caratteri.";
        }

        if (errore != null) {
            request.setAttribute("errore", errore);
            request.getRequestDispatcher("home").forward(request, response);
            return;
        }

        try {
            boolean emailEsistente = registrazioneService.emailEsistente(email);

            if (emailEsistente) {
                request.setAttribute("errore", "Email già registrata");
                request.getRequestDispatcher("home").forward(request, response);
                return;
            }

            Utente nuovoUtente = new Utente();
            nuovoUtente.setNome(nome);
            nuovoUtente.setCognome(cognome);
            nuovoUtente.setEmail(email);
            nuovoUtente.setPassword(password);
            nuovoUtente.setTipo("Cliente");

            registrazioneService.registraUtente(nuovoUtente);

            Utente registrato = registrazioneService.login(email, password);
            HttpSession session = request.getSession();
            session.setAttribute("utente", registrato);
            session.setAttribute("successo", "Registrazione avvenuta con successo!");

            response.sendRedirect("home");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
