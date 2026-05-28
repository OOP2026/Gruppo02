package model;

public class Vincolo  {
    String VincoloGiornoSettimana;
    String VincoloOraInizio;
    String VincoloOraFine;
    public Vincolo(String VincoloGiornoSettimana, String VincoloOraInizio, String VincoloOraFine){
        this.VincoloGiornoSettimana=VincoloGiornoSettimana;
        this.VincoloOraInizio=VincoloOraInizio;
        this.VincoloOraFine=VincoloOraFine;
    }
    public String getVincoloGiornoSettimana(){return VincoloGiornoSettimana;}
    public String getVincoloOraInizio(){return VincoloOraInizio;}
    public String getVincoloOraFine(){return VincoloOraFine;}
}
