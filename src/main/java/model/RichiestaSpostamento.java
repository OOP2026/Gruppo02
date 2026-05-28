package model;

public class RichiestaSpostamento {
    private Lezione lezionedaSpostare;
    private String NuovoGiornoLezione;
    private String NuovaOraInizio;
    private String NuovaOraFine;
    private String stato;
    private String motivazione;

    public RichiestaSpostamento(Lezione lezionedaSpostare, String NuovoGiornoLezione, String NuovaOraInizio, String NuovaOraFine, String motivazione){
        this.lezionedaSpostare=lezionedaSpostare;
        this.NuovoGiornoLezione=NuovoGiornoLezione;
        this.NuovaOraInizio=NuovaOraInizio;
        this.NuovaOraFine=NuovaOraFine;
        this.motivazione = motivazione;
        this.stato="In attesa";
    }
    public Lezione getLezionedaSpostare(){
        return lezionedaSpostare;
    }
    public String getNuovoGiornoLezione(){
        return NuovoGiornoLezione;
    }
    public String getNuovaOraInizio(){
        return NuovaOraInizio;
    }
    public String getNuovaOraFine(){
        return NuovaOraFine;
    }
    public String getStato(){
        return stato;
    }
    public String getMotivazione() {
        return motivazione;
    }
    public void setStato(String stato) {
        this.stato = stato;
    }
}
