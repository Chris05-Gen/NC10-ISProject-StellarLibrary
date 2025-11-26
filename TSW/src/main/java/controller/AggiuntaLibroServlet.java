package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Libro;
import model.Genere;
import model.Utente;
import service.GestioneCatalogoService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/AggiuntaLibro")
public class AggiuntaLibroServlet extends HttpServlet {
    private final GestioneCatalogoService catalogoService = new GestioneCatalogoService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utente admin = (Utente) req.getSession().getAttribute("utente");

        if (admin == null || !"Admin".equals(admin.getTipo())) {
            resp.sendRedirect("/Interface/unauthorized.jsp");
            return;
        }
        try {
            List<Genere> generi = catalogoService.getAllGeneri();
            req.setAttribute("generi", generi);
            req.getRequestDispatcher("/Interface/aggiuntaLibro.jsp").forward(req, resp);
        } catch (Exception e) {
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
            if (errore != null) {
                List<Genere> generi = catalogoService.getAllGeneri();
                req.setAttribute("errore", errore);
                req.setAttribute("generi", generi);
                req.getRequestDispatcher("/Interface/aggiuntaLibro.jsp").forward(req, resp);
                return;
            }
        } catch (Exception e) {
            resp.sendRedirect("home");
            return;
        }

        Libro l = new Libro();
        l.setIsbn(isbn);
        l.setTitolo(titolo);
        l.setAutore(autore);
        l.setCasaEditrice(casaEditrice);
        l.setPagine(Integer.parseInt(pagineRaw));
        l.setCopertina(copertina);
        l.setAnnoPubblicazione(Integer.parseInt(annoRaw));
        l.setPrezzo(new BigDecimal(prezzoRaw));
        l.setIdGenere(Integer.parseInt(genereRaw));

        try {
            catalogoService.creaLibro(l);
        } catch (Exception e) {
            HttpSession session = req.getSession();
            session.setAttribute("erroreLibro", "Errore nell'inserimento del libro");
            resp.sendRedirect("AggiuntaLibro");
            return;
        }
        resp.sendRedirect("AggiuntaLibro?success=1");
    }
}
