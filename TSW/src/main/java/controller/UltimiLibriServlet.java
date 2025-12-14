package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Libro;
import service.GestioneCatalogoService;

import java.io.IOException;
import java.util.List;

@WebServlet("/home")
public class UltimiLibriServlet extends HttpServlet {

    private final GestioneCatalogoService catalogoService = new GestioneCatalogoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // 1️⃣ recupero ultimi N libri tramite service
            List<Libro> ultimiLibri = catalogoService.getUltimiLibri(6);

            // 2️⃣ passo i dati alla JSP
            request.setAttribute("ultimiLibri", ultimiLibri);

            RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
            rd.forward(request, response);

        } catch (IllegalArgumentException e) {
            // errore “previsto” dal contratto → messaggio utente
            request.setAttribute("errore", e.getMessage());
            request.getRequestDispatcher("/index.jsp").forward(request, response);

        } catch (RuntimeException e) {
            // errore tecnico / DB
            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Errore nel caricamento della pagina iniziale."
            );
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
