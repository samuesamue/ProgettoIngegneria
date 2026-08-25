package it.unibo;

import java.io.Serializable;
import java.time.*;
import java.util.Date;

public class Utente implements Serializable {
    private String nome;
    private String conome;
    private String mail;
    private Date eta;

    public Utente(String nome){
        this.nome=nome;
    }
}
