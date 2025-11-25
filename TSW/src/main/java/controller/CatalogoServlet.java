package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Genere;
import dao.GenereDAO;
import model.Libro;
import dao.LibroDAO;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;



@WebServlet("/catalogo")
public class CatalogoServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Alla prima chiamata sono tutti vuoti i parametri,
        // pertanto vengono prelevati tutti i libri
        String genere = request.getParameter("genere");
        Integer idGenere = genere != null && !genere.isEmpty() ? Integer.parseInt(genere) : null;
        Integer minPagine = parseIntSafe(request.getParameter("minPagine"));
        Integer anno = parseIntSafe(request.getParameter("anno"));
        BigDecimal prezzoMin = parseBD(request.getParameter("prezzoMin"));
        BigDecimal prezzoMax = parseBD(request.getParameter("prezzoMax"));
        LibroDAO dao = new LibroDAO();
        try {
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

            // Se c'è un errore, ricarica la pagina con il messaggio e non fa query
            // e recupera tutti i generi
            if (request.getAttribute("error") != null) {
                GenereDAO gdao = new GenereDAO();
                List<Genere> generi = gdao.findAll();
                request.setAttribute("generi", generi);
                request.getRequestDispatcher("/Interface/catalogo.jsp").forward(request, response);
                return;
            }
            // se invece vengono passati i parametri si esegue la query con i parametri passati
            List<Libro> lista = dao.cercaConFiltri(idGenere, anno, prezzoMin, prezzoMax, minPagine );
            GenereDAO gdao = new GenereDAO();
            List<Genere> generi = gdao.findAll();
            
            request.setAttribute("generi", generi);
            request.setAttribute("selectedGenere", idGenere);
            request.setAttribute("minPagine", minPagine);
            request.setAttribute("libri", lista);
            request.setAttribute("anno", anno);
            request.setAttribute("prezzoMin", prezzoMin);
            request.setAttribute("prezzoMax", prezzoMax);

            request.getRequestDispatcher("/Interface/catalogo.jsp").forward(request, response);
        } catch (SQLException e) {
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

