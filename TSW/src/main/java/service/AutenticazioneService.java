package service;

import dao.UtenteDAO;
import model.Utente;

public class AutenticazioneService {
    private final UtenteDAO utenteDAO = new UtenteDAO();

    public Utente login(String email, String password) {
        try {
            return utenteDAO.login(email, password);
        } catch (Exception e) {
            throw new RuntimeException("Errore durante il login", e);
        }
    }
}
