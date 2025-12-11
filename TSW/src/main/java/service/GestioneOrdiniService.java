package service;

import dao.*;
import model.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestioneOrdiniService {
    private final OrdineDAO ordineDAO = new OrdineDAO();
    private final MetodoPagamentoDAO pagamentoDAO = new MetodoPagamentoDAO();
    private final IndirizzoDAO indirizzoDAO = new IndirizzoDAO();
    private final CarrelloDAO carrelloDAO = new CarrelloDAO();
    private final ContieneDAO contieneDAO = new ContieneDAO();
    private final LibroDAO libroDAO = new LibroDAO();

    public List<Ordine> getOrdiniByUtente(int idUtente) {
        try {
            return ordineDAO.getOrdiniByUtente(idUtente);
        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero degli ordini dell'utente", e);
        }
    }

    public Map<Integer, MetodoPagamento> getMetodiPagamentoPerOrdini(List<Ordine> ordini) {
        try {
            Map<Integer, MetodoPagamento> mappa = new HashMap<>();
            for (Ordine ordine : ordini) {
                MetodoPagamento pagamento = pagamentoDAO.getMetodoById(ordine.getMetodoPagamento().getId());
                mappa.put(ordine.getId(), pagamento);
            }
            return mappa;
        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero dei metodi di pagamento", e);
        }
    }

    public Map<Integer, Indirizzo> getIndirizziPerOrdini(List<Ordine> ordini) {
        try {
            Map<Integer, Indirizzo> mappa = new HashMap<>();
            for (Ordine ordine : ordini) {
                Indirizzo indirizzo = indirizzoDAO.getIndirizzoById(ordine.getIndirizzo().getId());
                mappa.put(ordine.getId(), indirizzo);
            }
            return mappa;
        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero degli indirizzi", e);
        }
    }

    public boolean indirizzoAppartieneAUtente(int idIndirizzo, int idUtente) {
        try {
            return indirizzoDAO.AppartieneA(idIndirizzo, idUtente);
        } catch (Exception e) {
            throw new RuntimeException("Errore nel controllo indirizzo", e);
        }
    }

    public boolean metodoPagamentoEsiste(int idMetodo) {
        try {
            return pagamentoDAO.Esiste(idMetodo);
        } catch (Exception e) {
            throw new RuntimeException("Errore nel controllo metodo di pagamento", e);
        }
    }

    public Carrello getCarrelloByUtente(int idUtente) {
        try {
            return carrelloDAO.findByUtenteId(idUtente);
        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero carrello utente", e);
        }
    }

    public List<Contiene> getContenutoCarrello(int idCarrello) {
        try {
            return contieneDAO.getContenuto(idCarrello);
        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero contenuto carrello", e);
        }
    }

    public BigDecimal calcolaTotaleOrdine(List<Contiene> items) {
        try {
            BigDecimal totale = BigDecimal.ZERO;

            for (Contiene c : items) {
                Libro libro = c.getLibro();
                if (libro == null) {
                    throw new IllegalStateException("Elemento Contiene senza Libro associato");
                }

                BigDecimal prezzo = libro.getPrezzo();
                if (prezzo == null) {
                    // se il DAO di Contiene non ti popola il prezzo del libro
                    prezzo = libroDAO.getPrezzoByIsbn(libro.getIsbn());
                }

                BigDecimal sub = prezzo.multiply(BigDecimal.valueOf(c.getQuantita()));
                totale = totale.add(sub);
            }
            return totale;

        } catch (Exception e) {
            throw new RuntimeException("Errore nel calcolo del totale ordine", e);
        }
    }

    public int creaOrdine(Utente utente, Indirizzo indirizzo,
                          MetodoPagamento metodo, BigDecimal totale) {
        try {
            Ordine ordine = new Ordine(
                    0,
                    utente,
                    indirizzo,
                    metodo,
                    new Timestamp(System.currentTimeMillis()),
                    totale
            );
            return ordineDAO.creaOrdine(ordine);
        } catch (Exception e) {
            throw new RuntimeException("Errore nella creazione dell'ordine", e);
        }
    }


    public void svuotaCarrello(int idCarrello) {
        try {
            contieneDAO.svuotaCarrello(idCarrello);
        } catch (Exception e) {
            throw new RuntimeException("Errore nello svuotamento del carrello", e);
        }
    }

    public List<Indirizzo> getIndirizziByUtente(int idUtente) {
        try {
            return indirizzoDAO.getIndirizziByUtente(idUtente);
        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero degli indirizzi utente", e);
        }
    }

    public List<MetodoPagamento> getTuttiIMetodiPagamento() {
        try {
            return pagamentoDAO.getAll();
        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero dei metodi di pagamento", e);
        }
    }

    // ✅ Recupero indirizzo per ID (per creazione ordine UML)
    public Indirizzo getIndirizzoById(int id) {
        try {
            return indirizzoDAO.getIndirizzoById(id);
        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero dell'indirizzo", e);
        }
    }

    // ✅ Recupero metodo pagamento per ID (per creazione ordine UML)
    public MetodoPagamento getMetodoPagamentoById(int id) {
        try {
            return pagamentoDAO.getMetodoById(id);
        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero del metodo di pagamento", e);
        }
    }
    public List<Ordine> getOrdiniConUtente() {
        try {
            return ordineDAO.getOrdiniConUtente();
        } catch (Exception e) {
            throw new RuntimeException("Errore recupero ordini admin", e);
        }
    }

}
