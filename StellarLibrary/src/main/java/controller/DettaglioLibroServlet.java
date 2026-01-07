package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Libro;
import model.Recensione;
import service.GestioneCatalogoService;
import service.GestioneRecensioniService;

import java.io.IOException;
import java.util.List;

@WebServlet("/DettaglioLibroServlet")
public class DettaglioLibroServlet extends HttpServlet {

    private final GestioneCatalogoService catalogoService = new GestioneCatalogoService();
    private final GestioneRecensioniService recensioniService = new GestioneRecensioniService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String isbnRaw = request.getParameter("isbn");

        try {
            // 1️⃣ Validazione + recupero libro tramite service
            Libro libro = catalogoService.getLibroByISBN(isbnRaw);

            if (libro == null) {
                // Caso previsto: ISBN valido ma libro non esistente
                request.setAttribute("errore", "Libro non trovato.");
                request.getRequestDispatcher("home").forward(request, response);
                return;
            }

            // 2️⃣ Recupero recensioni tramite service
            List<Recensione> recensioni = recensioniService.getRecensioniByLibro(libro.getIsbn());

            // 3️⃣ Passaggio alla JSP
            request.setAttribute("libro", libro);
            request.setAttribute("recensioni", recensioni);

            request.getRequestDispatcher("/Interface/dettaglioLibro.jsp")
                    .forward(request, response);

        } catch (IllegalArgumentException e) {
            // Errori sugli input (es. ISBN sbagliato)
            request.setAttribute("errore", e.getMessage());
            request.getRequestDispatcher("home").forward(request, response);

        } catch (RuntimeException e) {
            // Errori DAO o di sistema
            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Errore durante il caricamento del dettaglio libro."
            );
        }
    }
}
