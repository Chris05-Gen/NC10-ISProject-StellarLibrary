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

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String genere = request.getParameter("genere");
        Integer idGenere = genere != null && !genere.isEmpty() ? Integer.parseInt(genere) : null;
        Integer minPagine = parseIntSafe(request.getParameter("minPagine"));
        Integer anno = parseIntSafe(request.getParameter("anno"));
        BigDecimal prezzoMin = parseBD(request.getParameter("prezzoMin"));
        BigDecimal prezzoMax = parseBD(request.getParameter("prezzoMax"));

        // Validazione base
        if (anno != null && (anno < 1000 || anno > java.time.Year.now().getValue())) {
            request.setAttribute("error", "Anno non valido!");
        }
        if (prezzoMin != null && prezzoMin.compareTo(BigDecimal.ZERO) < 0) {
            request.setAttribute("error", "Il prezzo minimo non può essere negativo!");
        }
        if (prezzoMax != null && prezzoMax.compareTo(BigDecimal.ZERO) < 0) {
            request.setAttribute("error", "Il prezzo massimo non può essere negativo!");
        }
        if (prezzoMin != null && prezzoMax != null && prezzoMin.compareTo(prezzoMax) > 0) {
            request.setAttribute("error", "Il prezzo minimo non può essere maggiore del massimo!");
        }
        if (minPagine != null && minPagine < 0) {
            request.setAttribute("error", "Il numero minimo di pagine non può essere negativo!");
        }

        try {
            List<Genere> generi = catalogoService.getAllGeneri();
            request.setAttribute("generi", generi);

            if (request.getAttribute("error") != null) {
                request.getRequestDispatcher("/Interface/catalogo.jsp").forward(request, response);
                return;
            }

            List<Libro> libri = catalogoService.cercaLibriConFiltri(idGenere, anno, prezzoMin, prezzoMax, minPagine);
            request.setAttribute("selectedGenere", idGenere);
            request.setAttribute("minPagine", minPagine);
            request.setAttribute("libri", libri);
            request.setAttribute("anno", anno);
            request.setAttribute("prezzoMin", prezzoMin);
            request.setAttribute("prezzoMax", prezzoMax);

            request.getRequestDispatcher("/Interface/catalogo.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private Integer parseIntSafe(String value) {
        try { return value != null && !value.isEmpty() ? Integer.parseInt(value) : null; }
        catch (Exception e) { return null; }
    }

    private BigDecimal parseBD(String value) {
        try { return value != null && !value.isEmpty() ? new BigDecimal(value) : null; }
        catch (Exception e) { return null; }
    }
}
