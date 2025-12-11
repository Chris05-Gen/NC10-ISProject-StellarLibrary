package service;

import dao.*;
import model.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collections;
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

    /**
     * Restituisce tutti gli ordini di un utente.
     *
     * Precondizioni:
     *  - idUtente > 0
     *
     * Postcondizioni:
     *  - lista non nulla (può essere vuota)
     */
    public List<Ordine> getOrdiniByUtente(int idUtente) {
        if (idUtente <= 0) {
            throw new IllegalArgumentException("Id utente non valido.");
        }

        try {
            List<Ordine> ordini = ordineDAO.getOrdiniByUtente(idUtente);
            return (ordini != null) ? ordini : Collections.emptyList();
        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero degli ordini dell'utente.", e);
        }
    }

    /**
     * Restituisce una mappa idOrdine -> MetodoPagamento per gli ordini forniti.
     *
     * Precondizioni:
     *  - ordini != null
     *  - nessun elemento di ordini è null
     *  - ogni ordine ha un idMetodoPagamento valido (>0)
     *
     * Postcondizioni:
     *  - mappa non nulla (può essere vuota)
     */
    public Map<Integer, MetodoPagamento> getMetodiPagamentoPerOrdini(List<Ordine> ordini) {
        if (ordini == null) {
            throw new IllegalArgumentException("La lista degli ordini non può essere null.");
        }

        try {
            Map<Integer, MetodoPagamento> mappa = new HashMap<>();

            for (Ordine ordine : ordini) {
                if (ordine == null) {
                    throw new IllegalStateException("Trovato un ordine null nella lista.");
                }

                int idMetodo = ordine.getIdMetodoPagamento();
                if (idMetodo <= 0) {
                    throw new IllegalStateException("Id metodo pagamento non valido per l'ordine " + ordine.getId());
                }

                MetodoPagamento pagamento = pagamentoDAO.getMetodoById(idMetodo);
                if (pagamento == null) {
                    throw new IllegalStateException("Metodo di pagamento non trovato per id " + idMetodo);
                }

                mappa.put(ordine.getId(), pagamento);
            }

            return mappa;
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero dei metodi di pagamento per gli ordini.", e);
        }
    }

    /**
     * Restituisce una mappa idOrdine -> Indirizzo per gli ordini forniti.
     *
     * Precondizioni:
     *  - ordini != null
     *  - nessun elemento di ordini è null
     *  - ogni ordine ha un idIndirizzo valido (>0)
     *
     * Postcondizioni:
     *  - mappa non nulla (può essere vuota)
     */
    public Map<Integer, Indirizzo> getIndirizziPerOrdini(List<Ordine> ordini) {
        if (ordini == null) {
            throw new IllegalArgumentException("La lista degli ordini non può essere null.");
        }

        try {
            Map<Integer, Indirizzo> mappa = new HashMap<>();

            for (Ordine ordine : ordini) {
                if (ordine == null) {
                    throw new IllegalStateException("Trovato un ordine null nella lista.");
                }

                int idIndirizzo = ordine.getIdIndirizzo();
                if (idIndirizzo <= 0) {
                    throw new IllegalStateException("Id indirizzo non valido per l'ordine " + ordine.getId());
                }

                Indirizzo indirizzo = indirizzoDAO.getIndirizzoById(idIndirizzo);
                if (indirizzo == null) {
                    throw new IllegalStateException("Indirizzo non trovato per id " + idIndirizzo);
                }

                mappa.put(ordine.getId(), indirizzo);
            }

            return mappa;
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero degli indirizzi per gli ordini.", e);
        }
    }

    /**
     * Verifica se un indirizzo appartiene a un utente.
     *
     * Precondizioni:
     *  - idIndirizzo > 0
     *  - idUtente > 0
     */
    public boolean indirizzoAppartieneAUtente(int idIndirizzo, int idUtente) {
        if (idIndirizzo <= 0 || idUtente <= 0) {
            throw new IllegalArgumentException("Id indirizzo o utente non validi.");
        }

        try {
            Boolean appartiene = indirizzoDAO.AppartieneA(idIndirizzo, idUtente);

            if (appartiene == null) {
                throw new IllegalStateException("Il DAO ha restituito null per il controllo indirizzo/utente.");
            }

            return appartiene;
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Errore nel controllo appartenenza indirizzo.", e);
        }
    }

    /**
     * Controlla se esiste un metodo di pagamento con l'id passato.
     *
     * Precondizioni:
     *  - idMetodo > 0
     */
    public boolean metodoPagamentoEsiste(int idMetodo) {
        if (idMetodo <= 0) {
            throw new IllegalArgumentException("Id metodo di pagamento non valido.");
        }

        try {
            Boolean esiste = pagamentoDAO.Esiste(idMetodo);

            if (esiste == null) {
                throw new IllegalStateException("Il DAO ha restituito null per il controllo esistenza metodo pagamento.");
            }

            return esiste;
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Errore nel controllo metodo di pagamento.", e);
        }
    }

    /**
     * Restituisce il carrello dell'utente.
     *
     * Precondizioni:
     *  - idUtente > 0
     *
     * Postcondizioni:
     *  - se non esiste carrello → IllegalStateException
     */
    public Carrello getCarrelloByUtente(int idUtente) {
        if (idUtente <= 0) {
            throw new IllegalArgumentException("Id utente non valido.");
        }

        try {
            Carrello carrello = carrelloDAO.findByUtenteId(idUtente);

            if (carrello == null) {
                throw new IllegalStateException("Nessun carrello trovato per l'utente " + idUtente);
            }

            return carrello;
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero del carrello utente.", e);
        }
    }

    /**
     * Restituisce il contenuto del carrello.
     *
     * Precondizioni:
     *  - idCarrello > 0
     *
     * Postcondizioni:
     *  - lista non nulla (può essere vuota)
     */
    public List<Contiene> getContenutoCarrello(int idCarrello) {
        if (idCarrello <= 0) {
            throw new IllegalArgumentException("Id carrello non valido.");
        }

        try {
            List<Contiene> items = contieneDAO.getContenuto(idCarrello);
            return (items != null) ? items : Collections.emptyList();
        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero del contenuto del carrello.", e);
        }
    }

    /**
     * Calcola il totale dell'ordine dato il contenuto del carrello.
     *
     * Precondizioni:
     *  - items != null
     *  - nessun elemento null
     *  - quantita > 0
     *  - libro non null
     *  - prezzo >= 0
     */
    public BigDecimal calcolaTotaleOrdine(List<Contiene> items) {
        if (items == null) {
            throw new IllegalArgumentException("La lista degli elementi del carrello non può essere null.");
        }

        try {
            BigDecimal totale = BigDecimal.ZERO;

            for (Contiene c : items) {
                if (c == null) {
                    throw new IllegalStateException("Elemento null nella lista degli elementi del carrello.");
                }

                if (c.getQuantita() <= 0) {
                    throw new IllegalStateException("Quantità non valida per un elemento del carrello.");
                }

                Libro libro = c.getLibro();
                if (libro == null) {
                    throw new IllegalStateException("Elemento Contiene senza Libro associato.");
                }

                BigDecimal prezzo = libro.getPrezzo();
                if (prezzo == null) {
                    // se il DAO di Contiene non popola il prezzo del libro
                    prezzo = libroDAO.getPrezzoByIsbn(libro.getIsbn());
                    if (prezzo == null) {
                        throw new IllegalStateException("Prezzo non trovato per il libro con ISBN " + libro.getIsbn());
                    }
                }

                if (prezzo.compareTo(BigDecimal.ZERO) < 0) {
                    throw new IllegalStateException("Prezzo negativo per il libro con ISBN " + libro.getIsbn());
                }

                BigDecimal sub = prezzo.multiply(BigDecimal.valueOf(c.getQuantita()));
                totale = totale.add(sub);
            }

            return totale;
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Errore nel calcolo del totale ordine.", e);
        }
    }

    /**
     * Crea un ordine.
     *
     * Precondizioni:
     *  - idUtente > 0
     *  - idIndirizzo > 0
     *  - idMetodo > 0
     *  - totale != null e totale >= 0
     *
     * Postcondizioni:
     *  - idOrdine restituito > 0
     */
    public int creaOrdine(int idUtente, int idIndirizzo, int idMetodo, BigDecimal totale) {
        if (idUtente <= 0 || idIndirizzo <= 0 || idMetodo <= 0) {
            throw new IllegalArgumentException("Id utente, indirizzo o metodo pagamento non validi.");
        }
        if (totale == null || totale.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Totale ordine non valido.");
        }

        try {
            Ordine ordine = new Ordine(
                    0,
                    idUtente,
                    idIndirizzo,
                    idMetodo,
                    new Timestamp(System.currentTimeMillis()),
                    totale
            );

            int idOrdine = ordineDAO.creaOrdine(ordine);

            if (idOrdine <= 0) {
                throw new IllegalStateException("Creazione ordine non valida: idOrdine restituito <= 0.");
            }

            return idOrdine;
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Errore nella creazione dell'ordine.", e);
        }
    }

    /**
     * Svuota il carrello.
     *
     * Precondizioni:
     *  - idCarrello > 0
     */
    public void svuotaCarrello(int idCarrello) {
        if (idCarrello <= 0) {
            throw new IllegalArgumentException("Id carrello non valido.");
        }

        try {
            contieneDAO.svuotaCarrello(idCarrello);
        } catch (Exception e) {
            throw new RuntimeException("Errore nello svuotamento del carrello.", e);
        }
    }

    /**
     * Restituisce tutti gli indirizzi di un utente.
     *
     * Precondizioni:
     *  - idUtente > 0
     *
     * Postcondizioni:
     *  - lista non nulla (può essere vuota)
     */
    public List<Indirizzo> getIndirizziByUtente(int idUtente) {
        if (idUtente <= 0) {
            throw new IllegalArgumentException("Id utente non valido.");
        }

        try {
            List<Indirizzo> indirizzi = indirizzoDAO.getIndirizziByUtente(idUtente);
            return (indirizzi != null) ? indirizzi : Collections.emptyList();
        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero degli indirizzi utente.", e);
        }
    }

    /**
     * Restituisce l'elenco di tutti i metodi di pagamento disponibili.
     *
     * Postcondizioni:
     *  - lista non nulla (può essere vuota)
     */
    public List<MetodoPagamento> getTuttiIMetodiPagamento() {
        try {
            List<MetodoPagamento> metodi = pagamentoDAO.getAll();
            return (metodi != null) ? metodi : Collections.emptyList();
        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero dei metodi di pagamento.", e);
        }
    }

    /**
     * Restituisce l'elenco degli ordini "completi" per l'admin, con utente, indirizzo e metodo associati.
     * Usa la struttura Map<String, Object> come nel DAO OrdineDAO.getOrdiniConUtente().
     *
     * Postcondizioni:
     *  - lista non nulla (può essere vuota)
     *  - ogni mappa contiene chiavi:
     *      - "id" (id ordine)
     *      - "idMetodoPagamento"
     *      - "idIndirizzo"
     *      - "metodoPagamento" (oggetto MetodoPagamento)
     *      - "indirizzo" (oggetto Indirizzo)
     */
    public List<Map<String, Object>> getOrdiniCompletiPerAdmin() {
        try {
            List<Map<String, Object>> ordini = ordineDAO.getOrdiniConUtente();

            if (ordini == null) {
                return Collections.emptyList();
            }

            for (Map<String, Object> ordine : ordini) {
                if (ordine == null) {
                    throw new IllegalStateException("Trovata una mappa null nella lista degli ordini.");
                }

                Object idMetodoObj = ordine.get("idMetodoPagamento");
                Object idIndirizzoObj = ordine.get("idIndirizzo");

                if (!(idMetodoObj instanceof Integer) || !(idIndirizzoObj instanceof Integer)) {
                    throw new IllegalStateException("Id metodo pagamento o indirizzo mancanti o non validi.");
                }

                int idMetodo = (Integer) idMetodoObj;
                int idIndirizzo = (Integer) idIndirizzoObj;

                MetodoPagamento metodo = pagamentoDAO.getMetodoById(idMetodo);
                Indirizzo indirizzo = indirizzoDAO.getIndirizzoById(idIndirizzo);

                if (metodo == null || indirizzo == null) {
                    throw new IllegalStateException("Metodo di pagamento o indirizzo non trovati per id ordine " + ordine.get("id"));
                }

                ordine.put("metodoPagamento", metodo);
                ordine.put("indirizzo", indirizzo);
            }

            return ordini;

        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero degli ordini completi per admin.", e);
        }
    }
    public int creaOrdineCompleto(int idUtente, String idIndirizzoRaw, String idMetodoRaw) {

        if (idUtente <= 0) {
            throw new IllegalArgumentException("Utente non valido.");
        }

        // VALIDAZIONE RAW INPUT
        if (idIndirizzoRaw == null || idMetodoRaw == null) {
            throw new IllegalArgumentException("Seleziona indirizzo e metodo di pagamento.");
        }

        int idIndirizzo;
        int idMetodo;
        try {
            idIndirizzo = Integer.parseInt(idIndirizzoRaw);
            idMetodo = Integer.parseInt(idMetodoRaw);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Valori non validi per indirizzo o metodo di pagamento.");
        }

        // VALIDAZIONE BUSINESS
        if (!indirizzoAppartieneAUtente(idIndirizzo, idUtente)) {
            throw new IllegalStateException("Indirizzo non valido.");
        }

        if (!metodoPagamentoEsiste(idMetodo)) {
            throw new IllegalStateException("Metodo di pagamento non valido.");
        }

        // RECUPERO CARRELLO
        Carrello carrello = getCarrelloByUtente(idUtente);

        List<Contiene> items = getContenutoCarrello(carrello.getId());
        if (items.isEmpty()) {
            throw new IllegalStateException("Carrello vuoto.");
        }

        // CALCOLO TOTALE
        BigDecimal totale = calcolaTotaleOrdine(items);

        // CREAZIONE ORDINE
        int idOrdine = creaOrdine(idUtente, idIndirizzo, idMetodo, totale);

        // SVUOTAMENTO CARRELLO
        svuotaCarrello(carrello.getId());

        return idOrdine;
    }

}
