package model;

import java.util.ArrayList;

public class Docente extends Utente{
    private ArrayList<Insegnamento> insegnamenti;
    private ArrayList<Vincolo> vincoli;
    private static final int Max_Vincolo=3;

    public Docente(String nome, String cognome, String email, String password) {
        super(nome, cognome, email, password);
        this.insegnamenti=new ArrayList<>();
        this.vincoli=new ArrayList<>();
    }

    public boolean aggiungiInsegnamento(Insegnamento insegnamento) {
        if (insegnamenti.contains(insegnamento)) {
            return false;
        } else {
            insegnamenti.add(insegnamento);
            return true;
        }
    }
        public void rimuoviInsegnamento(Insegnamento insegnamento){
            insegnamenti.remove(insegnamento);
        }

    public boolean aggiungiVincolo(Vincolo vincolo){
        if (vincoli.size()<Max_Vincolo){
            vincoli.add(vincolo);
            return true;
        }
        return false;
    }
    public void rimuoviVincolo(Vincolo vincolo){
        vincoli.remove(vincolo);
    }
    public java.util.ArrayList<Vincolo> getVincoli(){
        return this.vincoli;
    }
    public java.util.ArrayList<Insegnamento> getInsegnamenti() {
        return this.insegnamenti;
    }
}






