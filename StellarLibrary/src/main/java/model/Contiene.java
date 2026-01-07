// model/Contiene.java
package model;

public class Contiene {
    private Carrello carrello;
    private Libro libro;
    private int quantita;

    public Contiene() { }

    public Contiene(Carrello carrello, Libro libro, int quantita) {
        this.carrello = carrello;
        this.libro = libro;
        this.quantita = quantita;
    }

    public Carrello getCarrello() { return carrello; }
    public void setCarrello(Carrello carrello) { this.carrello = carrello; }

    public Libro getLibro() { return libro; }
    public void setLibro(Libro libro) { this.libro = libro; }

    public int getQuantita() { return quantita; }
    public void setQuantita(int quantita) { this.quantita = quantita; }
}
