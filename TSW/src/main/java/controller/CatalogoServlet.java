package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Genere;
import model.Libro;
import service.GestioneCatalogoService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/catalogo")
public class CatalogoServlet extends HttpServlet {

    private final GestioneCatalogoService catalogoService = new GestioneCatalogoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lettura parametri raw
        String genereRaw = request.getParameter("genere");
        String annoRaw = request.getParameter("anno");
        String prezzoMinRaw = request.getParameter("prezzoMin");
        String prezzoMaxRaw = request.getParameter("prezzoMax");
        String minPagineRaw = request.getParameter("minPagine");

        try {
            // 1️⃣ Validazione + conversione dei parametri nel service
            GestioneCatalogoService.FiltriRicerca filtri =
                    catalogoService.validaEPreparaFiltri(
                            genereRaw, annoRaw, prezzoMinRaw, prezzoMaxRaw, minPagineRaw);

            // 2️⃣ Recupero generi (sempre)
            List<Genere> generi = catalogoService.getAllGeneri();
            request.setAttribute("generi", generi);

            // 3️⃣ Esecuzione ricerca
            List<Libro> libri = catalogoService.cercaLibriConFiltri(
                    filtri.idGenere, filtri.anno, filtri.prezzoMin, filtri.prezzoMax, filtri.minPagine
            );

            // 4️⃣ Passaggio parametri alla JSP
            request.setAttribute("libri", libri);
            request.setAttribute("selectedGenere", filtri.idGenere);
            request.setAttribute("anno", filtri.anno);
            request.setAttribute("prezzoMin", filtri.prezzoMin);
            request.setAttribute("prezzoMax", filtri.prezzoMax);
            request.setAttribute("minPagine", filtri.minPagine);

            request.getRequestDispatcher("/Interface/catalogo.jsp").forward(request, response);

        } catch (IllegalArgumentException e) {
            // errore di validazione
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/Interface/catalogo.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Errore durante la ricerca nel catalogo", e);
        }
    }
}
