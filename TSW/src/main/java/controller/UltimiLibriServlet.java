package controller;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;


import dao.LibroDAO;
import model.Libro;

@WebServlet("/home") // o "/index", o "/start"
public class UltimiLibriServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {

            LibroDAO dao = new LibroDAO();
            List<Libro> ultimiLibri = dao.findUltimi(6);
            request.setAttribute("ultimiLibri", ultimiLibri);
            RequestDispatcher rd = request.getRequestDispatcher("Interface/index.jsp");
            rd.forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response); // delega al metodo GET
    }
}