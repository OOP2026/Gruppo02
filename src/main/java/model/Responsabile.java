package model;

import java.util.ArrayList;

public class Responsabile extends Docente{
    private ArrayList<Lezione>lezioniImpostate;
    public Responsabile(String nome, String cognome, String email, String password){
        super(nome, cognome, email, password);
        this.lezioniImpostate= new ArrayList<>();
    }
    public void addLezione(Lezione lezione) {
        this.lezioniImpostate.add(lezione);
    }
    public ArrayList<Lezione> getLezione() {
        return lezioniImpostate;
    }
}
