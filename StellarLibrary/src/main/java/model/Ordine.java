package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Ordine {

    private int id;

    // ✅ riferimenti UML
    private Utente utente;
    private Indirizzo indirizzo;
    private MetodoPagamento metodoPagamento;

    private Timestamp dataOrdine;
    private BigDecimal totale;

    public Ordine() {}

    public Ordine(int id, Utente utente, Indirizzo indirizzo,
                  MetodoPagamento metodoPagamento,
                  Timestamp dataOrdine, BigDecimal totale) {
        this.id = id;
        this.utente = utente;
        this.indirizzo = indirizzo;
        this.metodoPagamento = metodoPagamento;
        this.dataOrdine = dataOrdine;
        this.totale = totale;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Utente getUtente() { return utente; }
    public void setUtente(Utente utente) { this.utente = utente; }

    public Indirizzo getIndirizzo() { return indirizzo; }
    public void setIndirizzo(Indirizzo indirizzo) { this.indirizzo = indirizzo; }

    public MetodoPagamento getMetodoPagamento() { return metodoPagamento; }
    public void setMetodoPagamento(MetodoPagamento metodoPagamento) { this.metodoPagamento = metodoPagamento; }

    public Timestamp getDataOrdine() { return dataOrdine; }
    public void setDataOrdine(Timestamp dataOrdine) { this.dataOrdine = dataOrdine; }

    public BigDecimal getTotale() { return totale; }
    public void setTotale(BigDecimal totale) { this.totale = totale; }
}
