package service;

import dao.RecensioneDAO;
import model.Recensione;

import java.util.List;

public class GestioneRecensioniService {
    private final RecensioneDAO recensioneDAO = new RecensioneDAO();

    // Aggiunge una recensione al database
    public void aggiungiRecensione(Recensione recensione) {
        try {
            recensioneDAO.addRecensione(recensione);
        } catch (Exception e) {
            throw new RuntimeException("Errore durante l'aggiunta della recensione", e);
        }
    }

    // Recupera tutte le recensioni con nome utente (solo admin)
    public List<Recensione> getAllRecensioniConNomeUtente() {
        try {
            return recensioneDAO.getAllRecensioniConNomeUtente();
        } catch (Exception e) {
            throw new RuntimeException("Errore nell'accesso alle recensioni", e);
        }
    }

    // Elimina la recensione tramite ID
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

