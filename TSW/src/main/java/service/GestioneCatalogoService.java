package service;

import dao.GenereDAO;
import dao.LibroDAO;
import model.Genere;
import model.Libro;

import java.math.BigDecimal;
import java.util.List;

public class GestioneCatalogoService {
    private final LibroDAO libroDAO = new LibroDAO();
    private final GenereDAO genereDAO = new GenereDAO();

    // Recupera tutti i generi disponibili
    public List<Genere> getAllGeneri() {
        try {
            return genereDAO.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero dei generi", e);
        }
    }

    // Cerca libri tramite filtri
    public List<Libro> cercaLibriConFiltri(Integer idGenere, Integer anno, BigDecimal prezzoMin, BigDecimal prezzoMax, Integer minPagine) {
        try {
            return libroDAO.cercaConFiltri(idGenere, anno, prezzoMin, prezzoMax, minPagine);
        } catch (Exception e) {
            throw new RuntimeException("Errore nella ricerca dei libri con filtri", e);
        }
    }

    // Cerca libro tramite ISBN
    public Libro getLibroByISBN(String isbn) {
        try {
            return libroDAO.findByISBN(isbn);
        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero del libro", e);
        }
    }
    // Crea libro nel database
    public void creaLibro(Libro libro) {
        try {
            libroDAO.create(libro);
        } catch (Exception e) {
            throw new RuntimeException("Errore durante la creazione del libro", e);
        }
    }
}
