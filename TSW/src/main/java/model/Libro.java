package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Libro {
    private String isbn;
    private String titolo;
    private String autore;
    private String casaEditrice;
    private int pagine;
    private String copertina;
    private int annoPubblicazione;
    private BigDecimal prezzo;

    private int idGenere;       // FK DB
    private Genere genere;      // riferimento OO

    // ✅ relazione UML: un libro ha più recensioni
    private List<Recensione> recensioni = new ArrayList<>();

    public List<Recensione> getRecensioni() {
        return recensioni;
    }

    public void addRecensione(Recensione r) {
        recensioni.add(r);
        r.setLibro(this);
    }
    public Genere getGenere() { return genere; }
    public void setGenere(Genere genere) { this.genere = genere; }
    // === GETTER & SETTER ===
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public String getCasaEditrice() {
        return casaEditrice;
    }

    public void setCasaEditrice(String casaEditrice) {
        this.casaEditrice = casaEditrice;
    }

    public int getPagine() {
        return pagine;
    }

    public void setPagine(int pagine) {
        this.pagine = pagine;
    }

    public String getCopertina() {
        return copertina;
    }

    public void setCopertina(String copertina) {
        this.copertina = copertina;
    }

    public int getAnnoPubblicazione() {
        return annoPubblicazione;
    }

    public void setAnnoPubblicazione(int annoPubblicazione) {
        this.annoPubblicazione = annoPubblicazione;
    }

    public BigDecimal getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
    }

    public int getIdGenere() {
        return idGenere;
    }

    public void setIdGenere(int idGenere) {
        this.idGenere = idGenere;
    }
}
