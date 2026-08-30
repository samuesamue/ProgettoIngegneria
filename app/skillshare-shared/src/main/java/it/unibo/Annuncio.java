package it.unibo;

import java.io.Serializable;
import java.util.UUID; //genera una chiave alfanumerica univoca per ogni annuncio

public class Annuncio implements Serializable {

    private String id; 
    private String titolo;
    private String descrizione;
    private String competenzaOfferta; 
    private String competenzaRichiesta; 
    private String autoreUsername; 

    public Annuncio() {
    }

    public Annuncio(String titolo, String descrizione, String competenzaOfferta, String competenzaRichiesta, String autoreUsername) {
        this.id = UUID.randomUUID().toString(); 
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.competenzaOfferta = competenzaOfferta;
        this.competenzaRichiesta = competenzaRichiesta;
        this.autoreUsername = autoreUsername;
    }

    //Metodi getter e setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getCompetenzaOfferta() {
        return competenzaOfferta;
    }

    public void setCompetenzaOfferta(String competenzaOfferta) {
        this.competenzaOfferta = competenzaOfferta;
    }

    public String getCompetenzaRichiesta() {
        return competenzaRichiesta;
    }

    public void setCompetenzaRichiesta(String competenzaRichiesta) {
        this.competenzaRichiesta = competenzaRichiesta;
    }

    public String getAutoreUsername() {
        return autoreUsername;
    }

    public void setAutoreUsername(String autoreUsername) {
        this.autoreUsername = autoreUsername;
    }
}