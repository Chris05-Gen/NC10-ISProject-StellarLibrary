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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            // 1. Validazione secondo il contratto del Service
            registrazioneService.validaDatiRegistrazione(nome, cognome, email, password);

            // 2. Verifica che l'email non sia già registrata
            if (registrazioneService.emailEsistente(email)) {
                request.setAttribute("errore", "Email già registrata.");
                request.getRequestDispatcher("home").forward(request, response);
                return;
            }

            // 3. Creazione dell'oggetto Utente
            Utente nuovoUtente = new Utente();
            nuovoUtente.setNome(nome);
            nuovoUtente.setCognome(cognome);
            nuovoUtente.setEmail(email);
            nuovoUtente.setPassword(password);
            nuovoUtente.setTipo("Cliente");

            // 4. Registrazione
            registrazioneService.registraUtente(nuovoUtente);

            // 5. Login automatico dopo la registrazione
            Utente registrato = registrazioneService.login(email, password);

            HttpSession session = request.getSession();
            session.setAttribute("utente", registrato);
            session.setAttribute("successo", "Registrazione avvenuta con successo!");

            response.sendRedirect("home");

        } catch (IllegalArgumentException e) {
            // Violazione del contratto → errore mostrato all'utente
            request.setAttribute("errore", e.getMessage());
            request.getRequestDispatcher("home").forward(request, response);

        } catch (RuntimeException e) {
            // Errori del DAO o di sistema → errore generico
            request.setAttribute("errore", "Si è verificato un errore interno.");
            request.getRequestDispatcher("home").forward(request, response);
        }
    }
}
