package model;

import java.sql.Date;

public class Recensione {

    private int id;

    // ✅ riferimenti UML
    private Utente utente;   // scrive
    private Libro libro;     // riguarda

    private String titolo;
    private String testo;
    private int valutazione;
    private Date data;

    // campo derivato (utile per la view)
    private String nomeUtente;

    public Recensione() {}

    public Recensione(int id, Utente utente, Libro libro,
                      String titolo, String testo,
                      int valutazione, Date data) {
        this.id = id;
        this.utente = utente;
        this.libro = libro;
        this.titolo = titolo;
        this.testo = testo;
        this.valutazione = valutazione;
        this.data = data;
    }

    // ===== GETTER & SETTER =====

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public int getValutazione() {
        return valutazione;
    }

    public void setValutazione(int valutazione) {
        this.valutazione = valutazione;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getNomeUtente() {
        return nomeUtente;
    }

    public void setNomeUtente(String nomeUtente) {
        this.nomeUtente = nomeUtente;
    }
}
