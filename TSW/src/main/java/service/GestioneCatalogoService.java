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

    // -------------------------------------------------------
    //  FILTRI CATALOGO
    // -------------------------------------------------------

    public static class FiltriRicerca {
        public Integer idGenere;
        public Integer anno;
        public BigDecimal prezzoMin;
        public BigDecimal prezzoMax;
        public Integer minPagine;
    }

    /**
     * Valida e converte i filtri di ricerca.
     *
     * Precondizioni:
     *  - raw parameter possono essere null o vuoti
     *
     * Postcondizioni:
     *  - ritorna un oggetto strutturato FiltriRicerca
     */
    public FiltriRicerca validaEPreparaFiltri(String idGenereRaw,
                                              String annoRaw,
                                              String prezzoMinRaw,
                                              String prezzoMaxRaw,
                                              String minPagineRaw) {

        FiltriRicerca filtri = new FiltriRicerca();

        // GENERE
        if (idGenereRaw != null && !idGenereRaw.isBlank()) {
            try {
                filtri.idGenere = Integer.parseInt(idGenereRaw);
                if (filtri.idGenere <= 0)
                    throw new IllegalArgumentException("Genere non valido.");
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Genere non valido.");
            }
        }

        // ANNO
        if (annoRaw != null && !annoRaw.isBlank()) {
            try {
                filtri.anno = Integer.parseInt(annoRaw);
                int currentYear = java.time.Year.now().getValue();
                if (filtri.anno < 1000 || filtri.anno > currentYear)
                    throw new IllegalArgumentException("Anno non valido.");
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Anno non valido.");
            }
        }

        // PREZZO MINIMO
        if (prezzoMinRaw != null && !prezzoMinRaw.isBlank()) {
            try {
                filtri.prezzoMin = new BigDecimal(prezzoMinRaw);
                if (filtri.prezzoMin.compareTo(BigDecimal.ZERO) < 0)
                    throw new IllegalArgumentException("Il prezzo minimo non può essere negativo.");
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Prezzo minimo non valido.");
            }
        }

        // PREZZO MASSIMO
        if (prezzoMaxRaw != null && !prezzoMaxRaw.isBlank()) {
            try {
                filtri.prezzoMax = new BigDecimal(prezzoMaxRaw);
                if (filtri.prezzoMax.compareTo(BigDecimal.ZERO) < 0)
                    throw new IllegalArgumentException("Il prezzo massimo non può essere negativo.");
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Prezzo massimo non valido.");
            }
        }

        // PREZZO MIN > MAX ?
        if (filtri.prezzoMin != null && filtri.prezzoMax != null &&
                filtri.prezzoMin.compareTo(filtri.prezzoMax) > 0) {
            throw new IllegalArgumentException("Il prezzo minimo non può essere maggiore del massimo.");
        }

        // MIN PAGINE
        if (minPagineRaw != null && !minPagineRaw.isBlank()) {
            try {
                filtri.minPagine = Integer.parseInt(minPagineRaw);
                if (filtri.minPagine < 0)
                    throw new IllegalArgumentException("Numero di pagine non valido.");
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Numero di pagine non valido.");
            }
        }

        return filtri;
    }

    // -------------------------------------------------------
    //  RICERCA CATALOGO
    // -------------------------------------------------------

    public List<Genere> getAllGeneri() {
        try {
            return genereDAO.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero dei generi", e);
        }
    }

    public List<Libro> cercaLibriConFiltri(Integer idGenere, Integer anno,
                                           BigDecimal prezzoMin, BigDecimal prezzoMax, Integer minPagine) {
        try {
            return libroDAO.cercaConFiltri(idGenere, anno, prezzoMin, prezzoMax, minPagine);
        } catch (Exception e) {
            throw new RuntimeException("Errore nella ricerca dei libri con filtri", e);
        }
    }

    public Libro getLibroByISBN(String isbn) {
        try {
            return libroDAO.findByISBN(isbn);
        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero del libro", e);
        }
    }

    // -------------------------------------------------------
    //  CREAZIONE LIBRO (ADMIN)
    // -------------------------------------------------------

    /**
     * Valida tutti i dati relativi alla creazione di un libro.
     * Design by Contract → perfetta per Category Partition.
     */
    public void validaDatiLibro(String isbn, String titolo, String autore, String pagineRaw,
                                String annoRaw, String prezzoRaw, String idGenereRaw) {

        if (isbn == null || !isbn.matches("\\d{10,13}"))
            throw new IllegalArgumentException("ISBN non valido.");

        if (titolo == null || titolo.isBlank())
            throw new IllegalArgumentException("Titolo mancante.");

        if (autore == null || autore.isBlank())
            throw new IllegalArgumentException("Autore mancante.");

        // pagine
        int pagine;
        try {
            pagine = Integer.parseInt(pagineRaw);
        } catch (Exception e) {
            throw new IllegalArgumentException("Numero di pagine non valido.");
        }
        if (pagine <= 0)
            throw new IllegalArgumentException("Il numero di pagine deve essere maggiore di 0.");

        // anno
        int anno;
        try {
            anno = Integer.parseInt(annoRaw);
        } catch (Exception e) {
            throw new IllegalArgumentException("Anno non valido.");
        }
        int currentYear = java.time.Year.now().getValue();
        if (anno < 1000 || anno > currentYear)
            throw new IllegalArgumentException("Anno di pubblicazione non valido.");

        // prezzo
        BigDecimal prezzo;
        try {
            prezzo = new BigDecimal(prezzoRaw);
        } catch (Exception e) {
            throw new IllegalArgumentException("Prezzo non valido.");
        }
        if (prezzo.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Il prezzo non può essere negativo.");

        // genere
        int idGenere;
        try {
            idGenere = Integer.parseInt(idGenereRaw);
        } catch (Exception e) {
            throw new IllegalArgumentException("Genere non selezionato.");
        }
        if (idGenere <= 0)
            throw new IllegalArgumentException("Id genere non valido.");
    }

    /**
     * Crea un libro nel database dopo che i dati sono stati validati.
     */
    public void creaLibro(String isbn, String titolo, String autore, String casaEditrice,
                          String pagineRaw, String copertina, String annoRaw, String prezzoRaw,
                          String idGenereRaw) {

        // Validazione (design by contract)
        validaDatiLibro(isbn, titolo, autore, pagineRaw, annoRaw, prezzoRaw, idGenereRaw);

        try {
            Libro l = new Libro();
            l.setIsbn(isbn);
            l.setTitolo(titolo);
            l.setAutore(autore);
            l.setCasaEditrice(casaEditrice);
            l.setPagine(Integer.parseInt(pagineRaw));
            l.setCopertina(copertina);
            l.setAnnoPubblicazione(Integer.parseInt(annoRaw));
            l.setPrezzo(new BigDecimal(prezzoRaw));
            l.setIdGenere(Integer.parseInt(idGenereRaw));

            libroDAO.create(l);

        } catch (Exception e) {
            throw new RuntimeException("Errore durante la creazione del libro.", e);
        }
    }
}
