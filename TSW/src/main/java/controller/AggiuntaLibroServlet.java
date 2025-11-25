package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import dao.GenereDAO;
import model.Libro;
import dao.LibroDAO;
import model.Utente;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

@WebServlet("/AggiuntaLibro")
public class AggiuntaLibroServlet extends HttpServlet {
    private final LibroDAO libroDAO = new LibroDAO();
    private final GenereDAO genereDAO = new GenereDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utente admin = (Utente) req.getSession().getAttribute("utente");

        // se non admin o non loggato, rimbalzo alla home
        if (admin == null || !"Admin".equals(admin.getTipo())) {
            resp.sendRedirect("/Interface/unauthorized.jsp");
            return;
        }
            // al primo caricamento della pagina ho
        // la necessità di recuperare tutti i generi dal db, per poter quindi inserire un libro
        try {
            req.setAttribute("generi", genereDAO.findAll());
            //invio i generi alla pagina e la chiamo
            req.getRequestDispatcher("/Interface/aggiuntaLibro.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect("/Interface/errore.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String isbn = req.getParameter("isbn");
        String titolo = req.getParameter("titolo");
        String autore = req.getParameter("autore");
        String casaEditrice = req.getParameter("casaEditrice");
        String pagineRaw = req.getParameter("pagine");
        String copertina = req.getParameter("copertina");
        String annoRaw = req.getParameter("annoPubblicazione");
        String prezzoRaw = req.getParameter("prezzo");
        String genereRaw = req.getParameter("idGenere");

        String errore = null;

        // recupero tutti i dati dal form e determino la loro validità
        if (isbn == null || !isbn.matches("\\d{10,13}")) {
            errore = "ISBN non valido.";
        } else if (titolo == null || titolo.isBlank()) {
            errore = "Titolo mancante.";
        } else if (autore == null || autore.isBlank()) {
            errore = "Autore mancante.";
        } else if (pagineRaw == null || !pagineRaw.matches("\\d+")) {
            errore = "Numero di pagine non valido.";
        } else if (annoRaw == null || !annoRaw.matches("\\d{4}")) {
            errore = "Anno di pubblicazione non valido.";
        } else if (prezzoRaw == null || !prezzoRaw.matches("\\d+(\\.\\d{1,2})?")) {
            errore = "Prezzo non valido.";
        } else if (genereRaw == null || !genereRaw.matches("\\d+")) {
            errore = "Genere non selezionato.";
        }
    try {
        // qualora ci fosse un errore lo segnalo,
        // ma comunque recupero i generi per far rimanere
        // la pagina di aggiunta coerente e funzionante
        if (errore != null) {
            req.setAttribute("errore", errore);
            req.setAttribute("generi", genereDAO.findAll()); // Riporta i generi nella pagina
            req.getRequestDispatcher("/Interface/aggiuntaLibro.jsp").forward(req, resp);
            return;
        }
    }catch (SQLException e) {
        e.printStackTrace();
        resp.sendRedirect("home");
        return;
    }
        // creo oggetto libro con tutti i dati del form
    Libro l = new Libro();
        l.setIsbn(req.getParameter("isbn"));
        l.setTitolo(req.getParameter("titolo"));
        l.setAutore(req.getParameter("autore"));
        l.setCasaEditrice(req.getParameter("casaEditrice"));
        l.setPagine(Integer.parseInt(req.getParameter("pagine")));
        l.setCopertina(req.getParameter("copertina"));
        l.setAnnoPubblicazione(Integer.parseInt(req.getParameter("annoPubblicazione")));
        l.setPrezzo(new BigDecimal(req.getParameter("prezzo")));
        l.setIdGenere(Integer.parseInt(req.getParameter("idGenere")));


        // lo creo nel DB, e ricarico
        // la pagina di aggiunta libro, segnalando che l'aggiunta è stata fatta con successo
        try {
            libroDAO.create(l);
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect("home");
            return;
        }
        resp.sendRedirect("AggiuntaLibro?success=1");
    }
}
