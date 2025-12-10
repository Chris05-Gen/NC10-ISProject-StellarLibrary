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
            if (utente != null) {
                Carrello carrello = carrelloDAO.findByUtente(utente);
                if (carrello == null)
                    carrello = carrelloDAO.createCarrello(utente);
                return carrello;
            } else {
                Integer carrelloId = (Integer) session.getAttribute("carrelloId");
                if (carrelloId != null) {
                    Carrello carrello = new Carrello();
                    carrello.setId(carrelloId);
                    carrello.setUtente(null);   // guest
                    return carrello;
                } else {
                    Carrello carrello = carrelloDAO.createCarrello(null);
                    session.setAttribute("carrelloId", carrello.getId());
                    return carrello;
                }
            }
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
        try {
            int quantita = contieneDAO.getQuantitaLibro(carrello.getId(), isbn);

            if (quantita > 1) {
                contieneDAO.decrementaQuantitaLibro(carrello.getId(), isbn);
            } else if (quantita == 1) {
                contieneDAO.rimuoviLibro(carrello.getId(), isbn);
            }

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

            if (carrelloUtente == null) {
                // utente non ha carrello: assegna direttamente quello guest
                Carrello guest = new Carrello();
                guest.setId(guestCarrelloId);
                carrelloDAO.assegnaCarrelloAUtente(guest, utente);
            } else {
                // fusione contenuti
                List<Contiene> guestContenuti = contieneDAO.getContenuto(guestCarrelloId);
                List<Contiene> userContenuti  = contieneDAO.getContenuto(carrelloUtente.getId());

                Map<String, Integer> userQuantita = new HashMap<>();
                for (Contiene c : userContenuti) {
                    userQuantita.put(c.getLibro().getIsbn(), c.getQuantita());
                }

                for (Contiene g : guestContenuti) {
                    String isbn = g.getLibro().getIsbn();
                    int nuovaQuantita = g.getQuantita();
                    if (userQuantita.containsKey(isbn)) {
                        nuovaQuantita += userQuantita.get(isbn);
                    }
                    contieneDAO.aggiungiLibro(carrelloUtente.getId(), isbn, nuovaQuantita);
                }

                Carrello guest = new Carrello();
                guest.setId(guestCarrelloId);
                carrelloDAO.eliminaCarrelloGuest(guest);
            }
        } catch (Exception e) {
            throw new RuntimeException("Errore nella fusione dei carrelli", e);
        }
    }


}