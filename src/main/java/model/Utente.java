package model;

public abstract class Utente {
    public  String nome, cognome, email;
    private String password;

    public Utente(String nome, String cognome, String email, String password){
        this.nome=nome;
        this.cognome=cognome;
        this.email=email;
        this.password=password;
    }

    public String getNome(){
        return nome;
    }
    public String getCognome(){
        return cognome;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public String getEmail(){
        return email;
    }
    public void setPassword(String password){
        this.password=password;
    }

    public String getPassword() {
        return password;
    }

    public boolean login (String email, String password){
        return(email.equals(this.email) && password.equals(this.password));
    }
}