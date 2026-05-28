package model;

public class Lezione {
    public String GiornoSettimana, Orainizio, Orafine;

    public Aula aula;
    public Responsabile responsabile;
    public Insegnamento insegnamento;
    public OrarioLezioni orarioLezioni;

    public Lezione(String GiornoSettimana, String Orainizio, String Orafine, Aula aula, Responsabile responsabile,Insegnamento insegnamento, OrarioLezioni orarioLezioni){
        this.GiornoSettimana =GiornoSettimana;
        this.Orainizio=Orainizio;
        this.Orafine=Orafine;
        this.aula=aula;
        this.responsabile=responsabile;
        this.insegnamento=insegnamento;
        this.orarioLezioni=orarioLezioni;
    }
    public Lezione (Insegnamento insegnamento,String giornoSettimana,String oraInizio, String oraFine, Aula aula){
        this.GiornoSettimana =giornoSettimana;
        this.Orainizio=oraInizio;
        this.Orafine=oraFine;
        this.aula=aula;
        this.insegnamento=insegnamento;
    }
    public Aula getAula() {
        return this.aula;
    }

    public void setGiornoSettimana(String giornoSettimana) {
        GiornoSettimana = giornoSettimana;
    }
    public String getGiornoSettimana(){
        return GiornoSettimana;
    }
    public void setOrainizio(String orainizio){
        Orainizio=orainizio;
    }
    public String getOrainizio(){
        return Orainizio;
    }
    public void setOrafine(String orafine){
        Orafine=orafine;
    }
    public String getOrafine(){
        return Orafine;
    }
    public Insegnamento getInsegnamento(){
        return this.insegnamento;
    }

}
