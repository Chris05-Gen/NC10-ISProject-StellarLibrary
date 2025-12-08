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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarrelloService {
    private final CarrelloDAO carrelloDAO = new CarrelloDAO();
    private final ContieneDAO contieneDAO = new ContieneDAO();
    private final LibroDAO libroDAO = new LibroDAO();

    // Recupera o crea il carrello (utente o guest)
    public Carrello getOrCreateCarrello(Utente utente, HttpSession session) {
        try {
            if (utente != null) {
                Carrello carrello = carrelloDAO.findByUtenteId(utente.getId());
                if (carrello == null)
                    carrello = carrelloDAO.createCarrello(utente.getId());
                return carrello;
            } else {
                Integer carrelloId = (Integer) session.getAttribute("carrelloId");
                if (carrelloId != null) {
                    Carrello carrello = new Carrello();
                    carrello.setId(carrelloId);
                    carrello.setIdUtente(-1);
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

    // Restituisce la lista degli item del carrello (libro + quantità + subtotale)
    public List<Map<String, Object>> getDettagliCarrello(Carrello carrello) {
        try {
            List<Contiene> contenuti = contieneDAO.getContenuto(carrello.getId());
            List<Map<String, Object>> dettagliCarrello = new ArrayList<>();
            for (Contiene c : contenuti) {
                Libro libro = libroDAO.findByISBN(c.getIsbn());
                BigDecimal subTotaleBD = libro.getPrezzo().multiply(BigDecimal.valueOf(c.getQuantita()));
                double subTotale = subTotaleBD.doubleValue();

                Map<String, Object> item = new HashMap<>();
                item.put("libro", libro);
                item.put("quantita", c.getQuantita());
                item.put("subTotale", subTotale);
                dettagliCarrello.add(item);
            }
            return dettagliCarrello;
        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero dettagli carrello", e);
        }
    }

    // Calcola il totale del carrello
    public double getTotaleCarrello(List<Map<String, Object>> dettagliCarrello) {
        BigDecimal totale = BigDecimal.ZERO;
        for (Map<String, Object> item : dettagliCarrello) {
            totale = totale.add(BigDecimal.valueOf((Double)item.get("subTotale")));
        }
        return totale.doubleValue();
    }

    // Rimuove una copia dal carrello, o tutto il libro se la quantità è 1
    public void rimuoviLibroDalCarrello(Carrello carrello, String isbn) {
        try {
            List<Contiene> contenuti = contieneDAO.getContenuto(carrello.getId());
            int quantita = 0;
            for (Contiene c : contenuti) {
                if (c.getIsbn().equals(isbn)) {
                    quantita = c.getQuantita();
                    break;
                }
            }
            if (quantita > 1) {
                contieneDAO.decrementaQuantitaLibro(carrello.getId(), isbn);
            } else if (quantita == 1) {
                contieneDAO.rimuoviLibro(carrello.getId(), isbn);
            }
            // Se il libro non era nel carrello non fa niente
        } catch (Exception e) {
            throw new RuntimeException("Errore nella rimozione dal carrello", e);
        }
    }


    // Aggiunge o aggiorna la quantità di un libro nel carrello
    public void aggiungiLibroAlCarrello(Carrello carrello, String isbn, int quantita) {
        try {
            List<Contiene> contenuti = contieneDAO.getContenuto(carrello.getId());
            int nuovaQuantita = quantita;
            for (Contiene c : contenuti) {
                if (c.getIsbn().equals(isbn)) {
                    nuovaQuantita += c.getQuantita();
                    break;
                }
            }
            contieneDAO.aggiungiLibro(carrello.getId(), isbn, nuovaQuantita);
        } catch (Exception e) {
            throw new RuntimeException("Errore nell'aggiunta libro al carrello", e);
        }
    }


    // Svuota tutto il contenuto del carrello
    public void svuotaCarrello(Carrello carrello) {
        try {
            contieneDAO.svuotaCarrello(carrello.getId());
        } catch (Exception e) {
            throw new RuntimeException("Errore durante lo svuotamento del carrello", e);
        }
    }

    public void unisciCarrelloGuestConUtente(Integer guestCarrelloId, Utente utente) {
        if (guestCarrelloId == null || utente == null) return;

        try {
            Carrello carrelloUtente = carrelloDAO.findByUtenteId(utente.getId());

            if (carrelloUtente == null) {
                // utente non ha carrello: assegna direttamente quello guest
                carrelloDAO.assegnaCarrelloAUtente(guestCarrelloId, utente.getId());
            } else {
                // fusione contenuti
                List<Contiene> guestContenuti = contieneDAO.getContenuto(guestCarrelloId);
                List<Contiene> userContenuti = contieneDAO.getContenuto(carrelloUtente.getId());

                Map<String, Integer> userQuantita = new HashMap<>();
                for (Contiene c : userContenuti) {
                    userQuantita.put(c.getIsbn(), c.getQuantita());
                }

                for (Contiene g : guestContenuti) {
                    int nuovaQuantita = g.getQuantita();
                    if (userQuantita.containsKey(g.getIsbn())) {
                        nuovaQuantita += userQuantita.get(g.getIsbn());
                    }
                    contieneDAO.aggiungiLibro(carrelloUtente.getId(), g.getIsbn(), nuovaQuantita);
                }

                carrelloDAO.eliminaCarrelloGuest(guestCarrelloId);
            }
        } catch (Exception e) {
            throw new RuntimeException("Errore nella fusione dei carrelli", e);
        }
    }
}
