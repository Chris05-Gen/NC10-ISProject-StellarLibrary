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

@WebServlet(name = "DettaglioLibroServlet", value = "/DettaglioLibroServlet")
public class DettaglioLibroServlet extends HttpServlet {
    private final GestioneCatalogoService catalogoService = new GestioneCatalogoService();
    private final GestioneRecensioniService recensioniService = new GestioneRecensioniService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String isbn = request.getParameter("isbn");
        if (isbn == null || isbn.isEmpty()) {
            response.sendRedirect("home");
            return;
        }

        try {
            Libro libro = catalogoService.getLibroByISBN(isbn);
            if (libro == null) {
                response.sendRedirect("home");
                return;
            }

            List<Recensione> recensioni = recensioniService.getRecensioniByLibro(isbn);

            request.setAttribute("libro", libro);
            request.setAttribute("recensioni", recensioni);

            request.getRequestDispatcher("/Interface/dettaglioLibro.jsp").forward(request, response);

        } catch (Exception e) {
            response.sendRedirect("/Interface/errore.jsp");
        }
    }
}
