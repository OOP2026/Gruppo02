package model;

import java.util.ArrayList;


public class Aula{
    private String nome;

    private ArrayList<Lezione> lezioni;

    public Aula(String nome){
        this.nome = nome;
        this.lezioni = new ArrayList<>();
    }

    public String getNome(){
        return nome;
    }
    public void setNome(String nome){
        this.nome = nome;
    }
    public ArrayList<Lezione> getLezioni(){
        return lezioni;
    }
    public void addLezione(Lezione lezione){
        this.lezioni.add(lezione);
    }
}