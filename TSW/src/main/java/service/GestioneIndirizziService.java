package service;

import dao.IndirizzoDAO;
import model.Indirizzo;

public class GestioneIndirizziService {

    private final IndirizzoDAO indirizzoDAO = new IndirizzoDAO();

    /**
     * Crea un indirizzo per un utente.
     *
     * Precondizioni:
     *  - idUtente > 0
     *  - via, citta, cap, provincia, nazione non null o vuoti
     *  - telefono non null e valido (10-15 cifre)
     *
     * Postcondizioni:
     *  - nuovo indirizzo inserito nel DB
     */
    public void creaIndirizzoPerUtente(int idUtente,
                                       String via,
                                       String citta,
                                       String cap,
                                       String provincia,
                                       String nazione,
                                       String telefono) {

        if (idUtente <= 0)
            throw new IllegalArgumentException("Utente non valido.");

        // VALIDAZIONE DATI
        if (via == null || via.isBlank())
            throw new IllegalArgumentException("La via è obbligatoria.");

        if (citta == null || citta.isBlank())
            throw new IllegalArgumentException("La città è obbligatoria.");

        if (cap == null || !cap.matches("\\d{5}"))
            throw new IllegalArgumentException("CAP non valido.");

        if (provincia == null || provincia.isBlank())
            throw new IllegalArgumentException("Provincia obbligatoria.");

        if (nazione == null || nazione.isBlank())
            throw new IllegalArgumentException("Nazione obbligatoria.");

        if (telefono == null || !telefono.matches("\\+?\\d{8,15}"))
            throw new IllegalArgumentException("Numero di telefono non valido.");

        try {
            Indirizzo nuovo = new Indirizzo();
            nuovo.setIdUtente(idUtente);
            nuovo.setVia(via);
            nuovo.setCitta(citta);
            nuovo.setCap(cap);
            nuovo.setProvincia(provincia);
            nuovo.setNazione(nazione);
            nuovo.setTelefono(telefono);

            indirizzoDAO.addIndirizzo(nuovo);

        } catch (Exception e) {
            throw new RuntimeException("Errore nella creazione dell'indirizzo.", e);
        }
    }
}
