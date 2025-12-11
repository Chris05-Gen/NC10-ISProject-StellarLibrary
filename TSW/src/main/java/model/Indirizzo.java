package model;

public class Indirizzo {

    private int id;

    // ✅ riferimento UML
    private Utente utente;

    private String via;
    private String citta;
    private String cap;
    private String provincia;
    private String nazione;
    private String telefono;

    public Indirizzo() {}

    public Indirizzo(int id, Utente utente, String via, String citta,
                     String cap, String provincia, String nazione, String telefono) {
        this.id = id;
        this.utente = utente;
        this.via = via;
        this.citta = citta;
        this.cap = cap;
        this.provincia = provincia;
        this.nazione = nazione;
        this.telefono = telefono;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Utente getUtente() { return utente; }
    public void setUtente(Utente utente) { this.utente = utente; }

    public String getVia() { return via; }
    public void setVia(String via) { this.via = via; }

    public String getCitta() { return citta; }
    public void setCitta(String citta) { this.citta = citta; }

    public String getCap() { return cap; }
    public void setCap(String cap) { this.cap = cap; }

    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }

    public String getNazione() { return nazione; }
    public void setNazione(String nazione) { this.nazione = nazione; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
