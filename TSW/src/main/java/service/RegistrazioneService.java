package service;

import dao.UtenteDAO;
import model.Utente;

public class RegistrazioneService {
    private final UtenteDAO utenteDAO = new UtenteDAO();

    public boolean emailEsistente(String email) {
        try {
            return utenteDAO.emailEsistente(email);
        } catch (Exception e) {
            throw new RuntimeException("Errore nel controllo email esistente", e);
        }
    }

    public void registraUtente(Utente utente) {
        try {
            utenteDAO.registraUtente(utente);
        } catch (Exception e) {
            throw new RuntimeException("Errore nella registrazione utente", e);
        }
    }

    public Utente login(String email, String password) {
        try {
            return utenteDAO.login(email, password);
        } catch (Exception e) {
            throw new RuntimeException("Errore nel login dopo registrazione", e);
        }
    }
}
