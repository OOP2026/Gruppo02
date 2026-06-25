package controller;

import dao.*;
import implementazioneDao.*;
import model.*;
import java.util.ArrayList;

public class Controller {

	// --- DAO ---
	private UtenteDAO utenteDAO = new UtentePostgresDAO();
	private LezioneDAO lezioneDAO = new LezionePostgresDAO();
	private AulaDAO aulaDAO = new AulaPostgresDAO();
	private InsegnamentoDAO insegnamentoDAO = new InsegnamentoPostgresDAO();
	private VincoloDAO vincoloDAO = new VincoloPostgresDAO();
	private RichiestaSpostamentoDAO richiestaDAO = new RichiestaSpostamentoPostgresDAO();

	// --- Liste residue (per oggetti non ancora migrati) ---
	private ArrayList<Studente> studenti = new ArrayList<>();
	private ArrayList<Docente> docenti = new ArrayList<>();
	private ArrayList<Responsabile> responsabili = new ArrayList<>();
	private Utente utenteLoggato = null;

	public Controller() {}

	// --- METODI UTENTE ---
	public void registraStudente(Studente studente) { utenteDAO.registraStudente(studente); }
	public void registraDocente(Docente docente) {
		utenteDAO.registraDocente(docente);
		docenti.add(docente);
	}
	public boolean effettuaLogin(String email, String password) {
		utenteLoggato = utenteDAO.login(email, password);
		return utenteLoggato != null;
	}
	public Utente getUtenteLoggato() { return utenteLoggato; }
	public void setUtenteLoggato(Utente utente) { this.utenteLoggato = utente; }

	// --- METODI LEZIONE ---
	public ArrayList<Lezione> getTutteLezioni() { return lezioneDAO.getTutteLezioni(); }
	public int getNumeroLezioni() { return lezioneDAO.getTutteLezioni().size(); }
	public ArrayList<Lezione> getLezioniDelDocente(Docente prof) { return lezioneDAO.getLezioniDelDocente(prof.getEmail()); }

	// --- METODI AULA ---
	public ArrayList<Aula> getAule() { return aulaDAO.getTutteLeAule(); }
	public ArrayList<Aula> getaule() { return aulaDAO.getTutteLeAule(); }
	public int getNumeroAule() { return aulaDAO.getTutteLeAule().size(); }
	public void aggiungiAula(Aula a) { /* Gestire in base al requisito specifico */ }

	// --- METODI INSEGNAMENTO ---
	public ArrayList<Insegnamento> getInsegnamenti() { return insegnamentoDAO.getTuttiInsegnamenti(); }
	public void aggiungiInsegnamento(Insegnamento i) { insegnamentoDAO.inserisciInsegnamento(i); }
	public void rimuoviInsegnamento(Insegnamento i) { /* Logica eliminazione */ }
	public int getNumeroInsegnamenti() { return insegnamentoDAO.getTuttiInsegnamenti().size(); }

	// --- METODI VINCOLI ---
	public ArrayList<Vincolo> getVincoliDocente(Docente docente) { return vincoloDAO.getVincoliPerDocente(docente.getEmail()); }
	public void aggiungiVincolo(Docente docente, Vincolo v) { vincoloDAO.inserisciVincolo(v, docente.getEmail()); }

	// --- METODI RICHIESTE SPOSTAMENTO ---
	public void aggiungiRichiestaSpostamento(RichiestaSpostamento richiesta) { richiestaDAO.inserisciRichiesta(richiesta); }
	public ArrayList<RichiestaSpostamento> getRichiesteSpostamento() { return richiestaDAO.getTutteLeRichieste(); }
	public int getNumeroRichiesteInAttesa() {
		int count = 0;
		for (RichiestaSpostamento r : richiestaDAO.getTutteLeRichieste()) {
			if (r.getStato().equals("In attesa")) count++;
		}
		return count;
	}
	public void eliminaRichiesta(int indice) { /* Logica eliminazione */ }

	public String accettaRichiesta(int indice) {
		ArrayList<RichiestaSpostamento> richieste = richiestaDAO.getTutteLeRichieste();
		if (indice < 0 || indice >= richieste.size()) return "Errore indice";

		RichiestaSpostamento req = richieste.get(indice);
		Lezione lezione = req.getLezionedaSpostare();
		Docente docente = lezione.getInsegnamento().getDocente();

		// Verifica Vincoli
		for(Vincolo v : vincoloDAO.getVincoliPerDocente(docente.getEmail())){
			if(v.getVincoloGiornoSettimana().equalsIgnoreCase(req.getNuovoGiornoLezione())){
				if (isSovrapposto(req.getNuovaOraInizio(), req.getNuovaOraFine(), v.getVincoloOraInizio(), v.getVincoloOraFine())) {
					return "Impossibile approvare: viola un vincolo del Prof. " + docente.getCognome();
				}
			}
		}

		// Verifica Conflitti
		for (Lezione altraLezione : lezioneDAO.getTutteLezioni()) {
			if (altraLezione.getInsegnamento().getNome().equals(lezione.getInsegnamento().getNome())) continue;
			if (altraLezione.getGiornoSettimana().equalsIgnoreCase(req.getNuovoGiornoLezione())) {
				if (isSovrapposto(req.getNuovaOraInizio(), req.getNuovaOraFine(), altraLezione.getOrainizio(), altraLezione.getOrafine())) {
					return "Conflitto rilevato.";
				}
			}
		}

		lezione.setGiornoSettimana(req.getNuovoGiornoLezione());
		lezione.setOrainizio(req.getNuovaOraInizio());
		lezione.setOrafine(req.getNuovaOraFine());
		return "OK";
	}

	// --- LOGICA ACCESSORIA ---
	public ArrayList<String> rilevaConflitti() {
		ArrayList<String> conflitti = new ArrayList<>();
		ArrayList<Lezione> lezioni = lezioneDAO.getTutteLezioni();
		for (int i = 0; i < lezioni.size(); i++) {
			for (int j = i + 1; j < lezioni.size(); j++) {
				Lezione l1 = lezioni.get(i);
				Lezione l2 = lezioni.get(j);
				if (l1.getGiornoSettimana().equals(l2.getGiornoSettimana()) && l1.getOrainizio().equals(l2.getOrainizio())) {
					if (l1.getAula().getNome().equals(l2.getAula().getNome())) conflitti.add(l1.getAula().getNome() + " occupata.");
				}
			}
		}
		return conflitti;
	}

	private boolean isSovrapposto(String inizio1, String fine1, String inizio2, String fine2) {
		return inizio1.compareTo(fine2) < 0 && fine1.compareTo(inizio2) > 0;
	}

	public String[] getGiorniSettimana() {
		return new String[]{"Lunedi","Martedi","Mercoledi","Giovedi","Venerdi"};
	}

	public ArrayList<Docente> getDocenti() { return docenti; }
}