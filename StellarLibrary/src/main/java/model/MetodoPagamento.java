package model;


public class MetodoPagamento {
    private int id;
    private String tipo;
    private String circuito;
    private String descrizione;

    public MetodoPagamento() {}

    public MetodoPagamento(int id, String tipo, String circuito, String descrizione) {
        this.id = id;
        this.tipo = tipo;
        this.circuito = circuito;
        this.descrizione = descrizione;
    }

    // Getters & Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCircuito() {
        return circuito;
    }

    public void setCircuito(String circuito) {
        this.circuito = circuito;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
}

