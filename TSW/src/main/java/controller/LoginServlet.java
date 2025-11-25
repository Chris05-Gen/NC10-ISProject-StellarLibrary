package controller;

import dao.CarrelloDAO;
import dao.ContieneDAO;
import dao.UtenteDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

        UtenteDAO utenteDAO = new UtenteDAO();
        CarrelloDAO carrelloDAO = new CarrelloDAO();

        try {
            Utente utente = utenteDAO.login(email, password); // Autenticazione
            if (utente != null) {
                HttpSession session = request.getSession();
                session.setAttribute("utente", utente);

                // Controlliamo se esiste un carrello "guest" in sessione
                Integer guestCarrelloId = (Integer) session.getAttribute("carrelloId");

                if (guestCarrelloId != null) {
                    Carrello carrelloUtente = carrelloDAO.findByUtenteId(utente.getId());

                    if (carrelloUtente == null) {
                        // Utente non ha carrello ➜ assegna quello guest
                        carrelloDAO.assegnaCarrelloAUtente(guestCarrelloId, utente.getId());
                    } else {
                        // FUSIONE: utente ha già un carrello ➜ uniamo i contenuti
                        ContieneDAO contieneDAO = new ContieneDAO();
                        List<Contiene> guestContenuti = contieneDAO.getContenuto(guestCarrelloId);
                        List<Contiene> userContenuti = contieneDAO.getContenuto(carrelloUtente.getId());

                        // Creiamo mappa ISBN ➜ quantità per il carrello utente (mappo isbn con quantità di tale libro)
                        Map<String, Integer> userQuantità = new HashMap<>();
                        for (Contiene c : userContenuti) {
                            userQuantità.put(c.getIsbn(), c.getQuantita());
                        }

                        // Per ogni libro nel guest carrello: prendo la quantità, se è
                        // nella map, e quindi già nel carrello, alla quantità di tale libro aggiungo quella
                        // nella mappa
                        for (Contiene g : guestContenuti) {
                            int nuovaQuantità = g.getQuantita();
                            if (userQuantità.containsKey(g.getIsbn())) {
                                nuovaQuantità += userQuantità.get(g.getIsbn());
                            }
                            //aggiungo i libri che appaiano in guest nel carello dell'utente loggato
                            contieneDAO.aggiungiLibro(carrelloUtente.getId(), g.getIsbn(), nuovaQuantità);
                        }

                        // Elimina il carrello guest
                        carrelloDAO.eliminaCarrelloGuest(guestCarrelloId);
                    }

                    // In ogni caso, rimuoviamo il riferimento sessione
                    session.removeAttribute("carrelloId");
                }
                session.setAttribute("successo", "Login effettuato con successo!");
                response.sendRedirect("home");

            } else {
                HttpSession session = request.getSession();
                session.setAttribute("erroreLogin", "Credenziali non valide.");
                response.sendRedirect("home");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore durante il login.");
        }
    }
}

