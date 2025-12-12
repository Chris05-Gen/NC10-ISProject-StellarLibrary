package service;

import dao.CarrelloDAO;
import dao.ContieneDAO;
import dao.LibroDAO;
import jakarta.servlet.http.HttpSession;
import model.Carrello;
import model.Contiene;
import model.Libro;
import model.Utente;

import java.math.BigDecimal;
import java.util.*;

public class CarrelloService {
    private final CarrelloDAO carrelloDAO = new CarrelloDAO();
    private final ContieneDAO contieneDAO = new ContieneDAO();
    private final LibroDAO libroDAO = new LibroDAO();

    // Recupera o crea il carrello (utente o guest)
    public Carrello getOrCreateCarrello(Utente utente, HttpSession session) {
        try {
            Integer guestCarrelloId = (Integer) session.getAttribute("carrelloId");

            // ======================================================
            // 🟦 CASO GUEST
            // ======================================================
            if (utente == null) {
                if (guestCarrelloId != null) {
                    return new Carrello(guestCarrelloId, null);
                }

                Carrello nuovo = carrelloDAO.createCarrello(null);
                session.setAttribute("carrelloId", nuovo.getId());
                return nuovo;
            }

            // ======================================================
            // 🟩 CASO UTENTE LOGGATO
            // ======================================================

            // Se c'è un carrello guest → NON creiamo carrelli utente.
            if (guestCarrelloId != null) {
                return new Carrello(guestCarrelloId, null);
            }

            // Recupera il carrello dell'utente (se esiste)
            Carrello carrelloUtente = carrelloDAO.findByUtente(utente);

            if (carrelloUtente != null) {
                return carrelloUtente;
            }

            // Crea nuovo carrello utente
            return carrelloDAO.createCarrello(utente);

        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero/creazione carrello", e);
        }
    }






    // Dettagli carrello (libro + quantità + subtotale)
    public List<Map<String, Object>> getDettagliCarrello(Carrello carrello) {
        try {
            List<Contiene> contenuti = contieneDAO.getContenuto(carrello.getId());
            List<Map<String, Object>> dettagliCarrello = new ArrayList<>();

            for (Contiene c : contenuti) {
                Libro libro = c.getLibro();  // ✅ già valorizzato dal DAO

                BigDecimal subTotale = libro.getPrezzo()
                        .multiply(BigDecimal.valueOf(c.getQuantita()));

                Map<String, Object> item = new HashMap<>();
                item.put("libro", libro);
                item.put("quantita", c.getQuantita());
                item.put("subTotale", subTotale.doubleValue());

                dettagliCarrello.add(item);
            }
            return dettagliCarrello;
        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero dettagli carrello", e);
        }
    }
    public double getTotaleCarrello(List<Map<String, Object>> dettagliCarrello) {
        BigDecimal totale = BigDecimal.ZERO;
        for (Map<String, Object> item : dettagliCarrello) {
            totale = totale.add(BigDecimal.valueOf((Double) item.get("subTotale")));
        }
        return totale.doubleValue();
    }

    // Rimuove una copia o l'intero libro
    public void rimuoviLibroDalCarrello(Carrello carrello, String isbn) {

        if (carrello == null || carrello.getId() <= 0)
            throw new IllegalArgumentException("Carrello non valido.");

        if (isbn == null || isbn.trim().isEmpty())
            throw new IllegalArgumentException("ISBN mancante.");

        try {
            int quantita = contieneDAO.getQuantitaLibro(carrello.getId(), isbn);

            if (quantita <= 0)
                throw new IllegalArgumentException("Il libro non è presente nel carrello.");

            if (quantita > 1) {
                contieneDAO.decrementaQuantitaLibro(carrello.getId(), isbn);
            } else {
                contieneDAO.rimuoviLibro(carrello.getId(), isbn);
            }

        } catch (IllegalArgumentException e) {
            throw e; // propaghiamo gli errori previsti dal contratto
        } catch (Exception e) {
            throw new RuntimeException("Errore nella rimozione dal carrello", e);
        }
    }



    // Aggiunge o aggiorna quantità
    public void aggiungiLibroAlCarrello(Carrello carrello, String isbn, int quantita) {
        try {
            int quantitaAttuale = contieneDAO.getQuantitaLibro(carrello.getId(), isbn);
            int nuovaQuantita = quantitaAttuale + quantita;

            contieneDAO.aggiungiLibro(carrello.getId(), isbn, nuovaQuantita);
        } catch (Exception e) {
            throw new RuntimeException("Errore nell'aggiunta libro al carrello", e);
        }
    }



    public void svuotaCarrello(Carrello carrello) {

        if (carrello == null || carrello.getId() <= 0)
            throw new IllegalArgumentException("Carrello non valido.");

        try {
            contieneDAO.svuotaCarrello(carrello.getId());
        } catch (Exception e) {
            throw new RuntimeException("Errore durante lo svuotamento del carrello", e);
        }
    }


    // Merge carrello guest con carrello utente
    public void unisciCarrelloGuestConUtente(Integer guestCarrelloId, Utente utente) {
        if (guestCarrelloId == null || utente == null) return;

        try {
            Carrello carrelloUtente = carrelloDAO.findByUtente(utente);

            // ======================================================
            // 🟦 Se l'utente non ha carrello → assegno il guest
            // ======================================================
            if (carrelloUtente == null) {
                carrelloDAO.assegnaCarrelloAUtente(
                        new Carrello(guestCarrelloId, null),
                        utente
                );
                return;
            }

            // ======================================================
            // 🟩 Altrimenti → FUSIONE CONTENUTI
            // ======================================================
            List<Contiene> guestContenuti = contieneDAO.getContenuto(guestCarrelloId);
            List<Contiene> userContenuti = contieneDAO.getContenuto(carrelloUtente.getId());

            // Mappa delle quantità attuali dell'utente
            Map<String, Integer> userQuantita = new HashMap<>();
            for (Contiene c : userContenuti) {
                userQuantita.put(c.getLibro().getIsbn(), c.getQuantita());
            }

            // Merge
            for (Contiene g : guestContenuti) {
                String isbn = g.getLibro().getIsbn();
                int quantita = g.getQuantita();

                if (userQuantita.containsKey(isbn)) {
                    quantita += userQuantita.get(isbn);
                }

                contieneDAO.aggiungiLibro(carrelloUtente.getId(), isbn, quantita);
            }

            // Elimina il carrello guest
            carrelloDAO.eliminaCarrelloGuest(new Carrello(guestCarrelloId, null));

        } catch (Exception e) {
            throw new RuntimeException("Errore nella fusione dei carrelli", e);
        }
    }



}