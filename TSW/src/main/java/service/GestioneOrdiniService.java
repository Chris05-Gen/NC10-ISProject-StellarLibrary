package service;

import dao.*;
import model.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

public class GestioneOrdiniService {

    private final OrdineDAO ordineDAO;
    private final MetodoPagamentoDAO pagamentoDAO;
    private final IndirizzoDAO indirizzoDAO;
    private final CarrelloDAO carrelloDAO;
    private final ContieneDAO contieneDAO;
    private final LibroDAO libroDAO;

    // Costruttore di produzione (default)
    public GestioneOrdiniService() {
        this(new OrdineDAO(),
                new MetodoPagamentoDAO(),
                new IndirizzoDAO(),
                new CarrelloDAO(),
                new ContieneDAO(),
                new LibroDAO());
    }

    // Costruttore per test / DI
    public GestioneOrdiniService(OrdineDAO ordineDAO,
                                 MetodoPagamentoDAO pagamentoDAO,
                                 IndirizzoDAO indirizzoDAO,
                                 CarrelloDAO carrelloDAO,
                                 ContieneDAO contieneDAO,
                                 LibroDAO libroDAO) {
        this.ordineDAO = ordineDAO;
        this.pagamentoDAO = pagamentoDAO;
        this.indirizzoDAO = indirizzoDAO;
        this.carrelloDAO = carrelloDAO;
        this.contieneDAO = contieneDAO;
        this.libroDAO = libroDAO;
    }
    // =========================================================================
    //  RECUPERO DATI
    // =========================================================================

    public List<Ordine> getOrdiniByUtente(int idUtente) {
        try {
            List<Ordine> ordini = ordineDAO.getOrdiniByUtente(idUtente);

            for (Ordine o : ordini) {
                // arricchimento UML
                o.setIndirizzo(indirizzoDAO.getIndirizzoById(o.getIndirizzo().getId()));
                o.setMetodoPagamento(pagamentoDAO.getMetodoById(o.getMetodoPagamento().getId()));
            }

            return ordini;

        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero ordini utente", e);
        }
    }


    public List<Ordine> getOrdiniCompletiPerAdmin() {
        try {
            List<Ordine> ordini = ordineDAO.getOrdiniConUtente();
            return (ordini != null) ? ordini : Collections.emptyList();

        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero degli ordini completi per admin.", e);
        }
    }

    public List<Indirizzo> getIndirizziByUtente(int idUtente) {
        if (idUtente <= 0)
            throw new IllegalArgumentException("ID utente non valido.");

        try {
            List<Indirizzo> indirizzi = indirizzoDAO.getIndirizziByUtente(idUtente);
            return (indirizzi != null) ? indirizzi : Collections.emptyList();

        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero degli indirizzi dell'utente.", e);
        }
    }

    public List<MetodoPagamento> getTuttiIMetodiPagamento() {
        try {
            List<MetodoPagamento> metodi = pagamentoDAO.getAll();
            return (metodi != null) ? metodi : Collections.emptyList();

        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero dei metodi di pagamento.", e);
        }
    }

    // =========================================================================
    //  VALIDAZIONI
    // =========================================================================

    public boolean indirizzoAppartieneAUtente(int idIndirizzo, int idUtente) {
        if (idIndirizzo <= 0 || idUtente <= 0)
            throw new IllegalArgumentException("ID non validi.");

        try {
            Boolean valore = indirizzoDAO.AppartieneA(idIndirizzo, idUtente);
            if (valore == null)
                throw new IllegalStateException("Il DAO ha restituito null.");
            return valore;

        } catch (Exception e) {
            throw new RuntimeException("Errore nel controllo di appartenenza dell'indirizzo.", e);
        }
    }

    public boolean metodoPagamentoEsiste(int idMetodo) {
        if (idMetodo <= 0)
            throw new IllegalArgumentException("ID metodo non valido.");

        try {
            Boolean valore = pagamentoDAO.Esiste(idMetodo);
            if (valore == null)
                throw new IllegalStateException("Il DAO ha restituito null.");
            return valore;

        } catch (Exception e) {
            throw new RuntimeException("Errore nel controllo metodo di pagamento.", e);
        }
    }

    // =========================================================================
    //  CARRELLO
    // =========================================================================

    public Carrello getCarrelloByUtente(int idUtente) {
        if (idUtente <= 0)
            throw new IllegalArgumentException("ID utente non valido.");

        try {
            Carrello c = carrelloDAO.findByUtenteId(idUtente);
            if (c == null)
                throw new IllegalStateException("Carrello non trovato.");
            return c;

        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero del carrello.", e);
        }
    }

    public List<Contiene> getContenutoCarrello(int idCarrello) {
        if (idCarrello <= 0)
            throw new IllegalArgumentException("ID carrello non valido.");

        try {
            List<Contiene> items = contieneDAO.getContenuto(idCarrello);
            return (items != null) ? items : Collections.emptyList();

        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero del contenuto del carrello.", e);
        }
    }

    public BigDecimal calcolaTotaleOrdine(List<Contiene> items) {
        if (items == null)
            throw new IllegalArgumentException("Items null.");

        try {
            BigDecimal totale = BigDecimal.ZERO;

            for (Contiene c : items) {
                if (c == null)
                    throw new IllegalStateException("Elemento del carrello null.");

                if (c.getQuantita() <= 0)
                    throw new IllegalStateException("Quantità non valida.");

                Libro libro = c.getLibro();
                if (libro == null)
                    throw new IllegalStateException("Libro non trovato.");

                BigDecimal prezzo = libro.getPrezzo();
                if (prezzo == null)
                    prezzo = libroDAO.getPrezzoByIsbn(libro.getIsbn());

                if (prezzo == null)
                    throw new IllegalStateException("Prezzo non trovato.");

                totale = totale.add(prezzo.multiply(BigDecimal.valueOf(c.getQuantita())));
            }

            return totale;

        } catch (Exception e) {
            throw new RuntimeException("Errore nel calcolo del totale dell'ordine.", e);
        }
    }

    // =========================================================================
    //  CREAZIONE ORDINE UML
    // =========================================================================

    public int creaOrdine(Ordine ordine) {
        try {
            return ordineDAO.creaOrdine(ordine);
        } catch (Exception e) {
            throw new RuntimeException("Errore nella creazione dell'ordine.", e);
        }
    }

    public void svuotaCarrello(int idCarrello) {
        if (idCarrello <= 0)
            throw new IllegalArgumentException("ID carrello non valido.");

        try {
            contieneDAO.svuotaCarrello(idCarrello);
        } catch (Exception e) {
            throw new RuntimeException("Errore nello svuotamento del carrello.", e);
        }
    }

    // =========================================================================
    //  FLUSSO COMPLETO DI CHECKOUT
    // =========================================================================

    /**
     * 1. Valida gli input raw
     * 2. Controlla se indirizzo e metodo sono validi
     * 3. Recupera carrello e items
     * 4. Calcola totale
     * 5. Costruisce oggetti: Utente, Indirizzo, MetodoPagamento
     * 6. Crea ordine tramite DAO
     * 7. Svuota carrello
     */
    public int creaOrdineCompleto(int idUtente, String idIndirizzoRaw, String idMetodoRaw) {

        if (idUtente <= 0)
            throw new IllegalArgumentException("ID utente non valido.");

        if (idIndirizzoRaw == null || idMetodoRaw == null)
            throw new IllegalArgumentException("Parametri mancanti.");

        int idIndirizzo;
        int idMetodo;

        try {
            idIndirizzo = Integer.parseInt(idIndirizzoRaw);
            idMetodo = Integer.parseInt(idMetodoRaw);
        } catch (Exception e) {
            throw new IllegalArgumentException("Valori non numerici.");
        }

        // Validazioni business
        if (!indirizzoAppartieneAUtente(idIndirizzo, idUtente))
            throw new IllegalStateException("Indirizzo non valido.");

        if (!metodoPagamentoEsiste(idMetodo))
            throw new IllegalStateException("Metodo di pagamento non valido.");

        Carrello carrello = getCarrelloByUtente(idUtente);
        List<Contiene> items = getContenutoCarrello(carrello.getId());

        if (items.isEmpty())
            throw new IllegalStateException("Carrello vuoto.");

        BigDecimal totale = calcolaTotaleOrdine(items);

        Utente u = new Utente();
        u.setId(idUtente);

        Indirizzo indirizzo;
        MetodoPagamento metodo;

        try {
            indirizzo = indirizzoDAO.getIndirizzoById(idIndirizzo);
            metodo = pagamentoDAO.getMetodoById(idMetodo);
        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero dati ordine.", e);
        }

        Ordine ordine = new Ordine(
                0,
                u,
                indirizzo,
                metodo,
                new Timestamp(System.currentTimeMillis()),
                totale
        );

        int idOrdine = creaOrdine(ordine);

        svuotaCarrello(carrello.getId());

        return idOrdine;
    }
    public Indirizzo getIndirizzoById(int id) {
        try {
            return indirizzoDAO.getIndirizzoById(id);
        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero indirizzo", e);
        }
    }

    public MetodoPagamento getMetodoPagamentoById(int id) {
        try {
            return pagamentoDAO.getMetodoById(id);
        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero metodo pagamento", e);
        }
    }

}
