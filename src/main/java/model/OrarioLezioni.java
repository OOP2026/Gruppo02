package model;

import java.util.ArrayList;

public class OrarioLezioni {
    private ArrayList<Lezione> lezioni;
    public OrarioLezioni() {
        this.lezioni = new ArrayList<>();
    }
    public ArrayList<Lezione> getLezioni() {
        return lezioni;
    }
    public void addLezione(Lezione lezione) {
        this.lezioni.add(lezione);
    }
}