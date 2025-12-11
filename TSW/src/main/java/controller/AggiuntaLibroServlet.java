package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Genere;
import model.Utente;
import service.GestioneCatalogoService;

import java.io.IOException;
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

        try {
            // Validazione + creazione
            catalogoService.creaLibro(
                    isbn, titolo, autore, casaEditrice,
                    pagineRaw, copertina, annoRaw, prezzoRaw, genereRaw
            );

            resp.sendRedirect("AggiuntaLibro?success=1");

        } catch (IllegalArgumentException e) {
            // Errori di input → torno al form con messaggio
            req.setAttribute("errore", e.getMessage());
            req.setAttribute("generi", catalogoService.getAllGeneri());
            req.getRequestDispatcher("/Interface/aggiuntaLibro.jsp").forward(req, resp);

        } catch (Exception e) {
            HttpSession session = req.getSession();
            session.setAttribute("erroreLibro", "Errore nell'inserimento del libro.");
            resp.sendRedirect("AggiuntaLibro");
        }
    }
}
