package service;

import dao.RecensioneDAO;
import model.Libro;
import model.Recensione;
import model.Utente;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class GestioneRecensioniService {

    private final RecensioneDAO recensioneDAO;

    public GestioneRecensioniService(RecensioneDAO recensioneDAO) {
        this.recensioneDAO = recensioneDAO;
    }

    // costruttore di default (se ti serve)
    public GestioneRecensioniService() {
        this.recensioneDAO = new RecensioneDAO();
    }
    /**
     * Valida tutti i campi della recensione.
     *
     * Precondizioni (Design by Contract):
     *  - utente != null
     *  - isbn non nullo né vuoto
     *  - titolo non nullo né vuoto
     *  - testo non nullo, non vuoto, <= 500 caratteri
     *  - valutazione ∈ [1,5]
     *
     * Se una precondizione fallisce → IllegalArgumentException
     */
    public void validaRecensione(String isbn, String titolo, String testo, String valutazioneRaw) {

        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN mancante.");
        }
        if (titolo == null || titolo.trim().isEmpty()) {
            throw new IllegalArgumentException("Titolo recensione obbligatorio.");
        }
        if (testo == null || testo.trim().isEmpty()) {
            throw new IllegalArgumentException("Il testo della recensione è obbligatorio.");
        }
        if (testo.length() > 500) {
            throw new IllegalArgumentException("La recensione è troppo lunga (max 500 caratteri).");
        }
        if (valutazioneRaw == null) {
            throw new IllegalArgumentException("Valutazione mancante.");
        }

        int valutazione;
        try {
            valutazione = Integer.parseInt(valutazioneRaw);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Valutazione non numerica.");
        }

        if (valutazione < 1 || valutazione > 5) {
            throw new IllegalArgumentException("Valutazione fuori range (1-5).");
        }
    }

    /**
     * Costruisce e salva una recensione.
     *
     * Precondizioni:
     *  - utente != null
     *  - isbn valido
     *  - titolo/testo/valutazione già validati da validaRecensione()
     *
     * Postcondizioni:
     *  - La recensione esiste nel DB
     */
    public void aggiungiRecensione(Utente utente, String isbn,
                                   String titolo, String testo, int valutazione) {

        if (utente == null) {
            throw new IllegalArgumentException("Utente non autenticato.");
        }

        // Creo l’oggetto Libro minimo
        Libro libro = new Libro();
        libro.setIsbn(isbn);

        Recensione recensione = new Recensione(
                0,
                utente,
                libro,
                titolo,
                testo,
                valutazione,
                Date.valueOf(LocalDate.now())
        );

        try {
            recensioneDAO.addRecensione(recensione);
        } catch (Exception e) {
            throw new RuntimeException("Errore durante il salvataggio della recensione", e);
        }
    }

    public List<Recensione> getAllRecensioniConNomeUtente() {
        try {
            return recensioneDAO.getAllRecensioniConNomeUtente();
        } catch (Exception e) {
            throw new RuntimeException("Errore nell'accesso alle recensioni", e);
        }
    }

    public void eliminaRecensione(int idRecensione) {
        try {
            recensioneDAO.deleteRecensioneById(idRecensione);
        } catch (Exception e) {
            throw new RuntimeException("Errore nell'eliminazione della recensione", e);
        }
    }

    public List<Recensione> getRecensioniByLibro(String isbn) {
        try {
            return recensioneDAO.getByLibro(isbn);
        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero delle recensioni", e);
        }
    }
}
