package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Genere;
import model.Libro;
import service.GestioneCatalogoService;

import java.io.IOException;
import java.util.List;

@WebServlet("/catalogo")
public class CatalogoServlet extends HttpServlet {

    private final GestioneCatalogoService catalogoService = new GestioneCatalogoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // raw input
        String genereRaw = request.getParameter("genere");
        String annoRaw = request.getParameter("anno");
        String prezzoMinRaw = request.getParameter("prezzoMin");
        String prezzoMaxRaw = request.getParameter("prezzoMax");
        String minPagineRaw = request.getParameter("minPagine");

        try {
            // 1️⃣ Validazione + parsing
            GestioneCatalogoService.FiltriRicerca filtri =
                    catalogoService.validaEPreparaFiltri(
                            genereRaw, annoRaw, prezzoMinRaw, prezzoMaxRaw, minPagineRaw);

            // 2️⃣ Recupero generi
            List<Genere> generi = catalogoService.getAllGeneri();
            request.setAttribute("generi", generi);

            // 3️⃣ Ricerca filtrata
            List<Libro> libri = catalogoService.cercaLibriConFiltri(
                    filtri.idGenere, filtri.anno, filtri.prezzoMin, filtri.prezzoMax, filtri.minPagine
            );

            // 4️⃣ Salvataggio parametri in JSP
            request.setAttribute("libri", libri);
            request.setAttribute("selectedGenere", filtri.idGenere);
            request.setAttribute("anno", filtri.anno);
            request.setAttribute("prezzoMin", filtri.prezzoMin);
            request.setAttribute("prezzoMax", filtri.prezzoMax);
            request.setAttribute("minPagine", filtri.minPagine);

            request.getRequestDispatcher("/Interface/catalogo.jsp").forward(request, response);

        } catch (IllegalArgumentException e) {

            // ❗ Errore previsto → rimango nella pagina catalogo con messaggio
            request.setAttribute("error", e.getMessage());
            request.setAttribute("generi", catalogoService.getAllGeneri());
            request.getRequestDispatcher("/Interface/catalogo.jsp").forward(request, response);

        } catch (Exception e) {

            // ❗ Errore imprevisto → Coerenza con tutte le altre servlet
            session.setAttribute("errore", "Errore durante la ricerca nel catalogo.");
            response.sendRedirect("home");
        }
    }
}
