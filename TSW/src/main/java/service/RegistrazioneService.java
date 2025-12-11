package service;

import dao.UtenteDAO;
import model.Utente;

public class RegistrazioneService {

    private final UtenteDAO utenteDAO = new UtenteDAO();

    /**
     * Applica il contratto di validazione dei dati di registrazione.
     *
     * Precondizioni:
     *  - nome non nullo né vuoto
     *  - cognome non nullo né vuoto
     *  - email valida (regex)
     *  - password lunga almeno 6 caratteri
     *
     * In caso di violazione → IllegalArgumentException
     */
    public void validaDatiRegistrazione(String nome, String cognome,
                                        String email, String password) {

        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome è obbligatorio.");
        }

        if (cognome == null || cognome.trim().isEmpty()) {
            throw new IllegalArgumentException("Il cognome è obbligatorio.");
        }

        if (email == null || !email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Email non valida.");
        }

        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("La password deve essere di almeno 6 caratteri.");
        }
    }

    /**
     * Contratto: email deve essere valida prima di essere verificata.
     * Lancia IllegalArgumentException se l'email non rispetta il formato.
     */
    public boolean emailEsistente(String email) {

        if (email == null || !email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Email non valida.");
        }

        try {
            return utenteDAO.emailEsistente(email);
        } catch (Exception e) {
            throw new RuntimeException("Errore nel controllo email esistente", e);
        }
    }

    /**
     * Postcondizione: se non viene lanciata eccezione, l'utente è registrato.
     */
    public void registraUtente(Utente utente) {

        if (utente == null) {
            throw new IllegalArgumentException("Utente nullo.");
        }

        try {
            utenteDAO.registraUtente(utente);
        } catch (Exception e) {
            throw new RuntimeException("Errore nella registrazione utente", e);
        }
    }

    /**
     * Postcondizione: se esiste, restituisce l'utente; altrimenti null.
     */
    public Utente login(String email, String password) {

        if (email == null || password == null) {
            throw new IllegalArgumentException("Email o password null.");
        }

        try {
            return utenteDAO.login(email, password);
        } catch (Exception e) {
            throw new RuntimeException("Errore nel login dopo registrazione", e);
        }
    }
}
