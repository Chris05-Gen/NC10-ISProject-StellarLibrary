// model/Carrello.java
package model;

import java.util.HashSet;
import java.util.Set;

public class Carrello {
    private int id;
    private Utente utente;            // 0..1 o 1, secondo UML
    private Set<Contiene> righe = new HashSet<>();

    public Carrello() { }

    public Carrello(int id, Utente utente) {
        this.id = id;
        this.utente = utente;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Utente getUtente() { return utente; }
    public void setUtente(Utente utente) { this.utente = utente; }

    public Set<Contiene> getRighe() { return righe; }

    // metodi di utilità stile UML
    public void addRiga(Contiene riga) {
        righe.add(riga);
        riga.setCarrello(this);
    }

    public void removeRiga(Contiene riga) {
        righe.remove(riga);
        riga.setCarrello(null);
    }
}
