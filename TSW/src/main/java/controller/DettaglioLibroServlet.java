package controller;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import model.Libro;
import dao.LibroDAO;
import model.Recensione;
import dao.RecensioneDAO;

@WebServlet(name = "DettaglioLibroServlet", value = "/DettaglioLibroServlet")
public class DettaglioLibroServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String isbn = request.getParameter("isbn");
        if (isbn == null || isbn.isEmpty()) {
            response.sendRedirect("home"); // oppure pagina home
            return;
        }

        LibroDAO libroDAO = new LibroDAO();
        RecensioneDAO recensioneDAO = new RecensioneDAO();

        try {
            Libro libro = libroDAO.findByISBN(isbn);
            List<Recensione> recensioni = recensioneDAO.getByLibro(isbn);

            if (libro == null) {
                response.sendRedirect("home"); // libro non trovato
                return;
            }

            request.setAttribute("libro", libro);
            request.setAttribute("recensioni", recensioni);

            request.getRequestDispatcher("/Interface/dettaglioLibro.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("/Interface/errore.jsp"); // log dettagliato lato server
        }
    }
}