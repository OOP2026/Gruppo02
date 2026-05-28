package model;

public class Insegnamento {
    public String nome;
    public int CFU, annoCorso;
    private Docente docente;
    public Insegnamento(String nome, int CFU, int annoCorso, Docente docente) {
        this.nome = nome;
        this.CFU = CFU;
        this.annoCorso = annoCorso;
        this.docente=docente;
    }
    public String getNome() {return nome;}
    public int getCFU(){return CFU;}
    public int getAnnoCorso(){return annoCorso;}
    public Docente getDocente(){
        return this.docente;
    }
}
