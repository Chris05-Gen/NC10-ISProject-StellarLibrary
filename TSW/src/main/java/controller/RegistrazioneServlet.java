package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Utente;
import service.CarrelloService;
import service.RegistrazioneService;

import java.io.IOException;

@WebServlet("/RegistrazioneServlet")
public class RegistrazioneServlet extends HttpServlet {

    private final RegistrazioneService registrazioneService = new RegistrazioneService();
    private final CarrelloService carrelloService = new CarrelloService(); // 👈 AGGIUNTO

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            registrazioneService.validaDatiRegistrazione(nome, cognome, email, password);

            if (registrazioneService.emailEsistente(email)) {
                request.setAttribute("errore", "Email già registrata.");
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

            // Login automatico
            Utente registrato = registrazioneService.login(email, password);

            HttpSession session = request.getSession();
            session.setAttribute("utente", registrato);
            session.setAttribute("successo", "Registrazione avvenuta con successo!");

            // 🔥 MERGE CARRELLO GUEST → UTENTE SUBITO DOPO REGISTRAZIONE
            Integer guestCarrelloId = (Integer) session.getAttribute("carrelloId");
            if (guestCarrelloId != null) {
                carrelloService.unisciCarrelloGuestConUtente(guestCarrelloId, registrato);
                session.removeAttribute("carrelloId");
            }

            response.sendRedirect("home");

        } catch (IllegalArgumentException e) {
            request.setAttribute("errore", e.getMessage());
            request.getRequestDispatcher("home").forward(request, response);

        } catch (RuntimeException e) {
            request.setAttribute("errore", "Errore interno durante la registrazione.");
            request.getRequestDispatcher("home").forward(request, response);
        }
    }
}

