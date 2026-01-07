package service;

import dao.UtenteDAO;
import model.Utente;

public class AutenticazioneService {

    private final UtenteDAO utenteDAO = new UtenteDAO();

    /**
     * Login con precondizioni (Design by Contract)
     *
     * Precondizioni:
     *  - email != null, non vuota, formato valido
     *  - password != null, non vuota
     *
     * Postcondizioni:
     *  - ritorna un Utente valido O null se credenziali sbagliate
     *
     * Eccezioni:
     *  - IllegalArgumentException se precondizioni violate
     *  - RuntimeException per errori DB
     */
    public Utente login(String email, String password) {

        // PRECONDIZIONI
        if (email == null || email.trim().isEmpty())
            throw new IllegalArgumentException("Email obbligatoria.");

        if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$"))
            throw new IllegalArgumentException("Formato email non valido.");

        if (password == null || password.trim().isEmpty())
            throw new IllegalArgumentException("Password obbligatoria.");

        try {
            return utenteDAO.login(email, password);
        } catch (Exception e) {
            throw new RuntimeException("Errore durante il login", e);
        }
    }
}
