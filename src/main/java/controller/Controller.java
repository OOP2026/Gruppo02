package controller;

import model.*;

import java.util.ArrayList;
public class Controller {

	// Liste che fungono da Database in memoria
	private ArrayList<Studente> studenti = new ArrayList<>();
	private ArrayList<Docente> docenti = new ArrayList<>();
	private ArrayList<Responsabile> responsabili = new ArrayList<>();
	private ArrayList<Insegnamento> insegnamenti = new ArrayList<>();
	private ArrayList<Lezione> lezioni = new ArrayList<>();
	private ArrayList<Aula> aule = new ArrayList<>();
	private ArrayList<RichiestaSpostamento> richieste = new ArrayList<>();
	private Utente utenteLoggato = null;
	private ArrayList<RichiestaSpostamento> richiesteSpostamento = new ArrayList<>();

	public void registraStudente(model.Studente studente) {
		studenti.add(studente);
	}

	public void registraDocente(model.Docente docente) {
		docenti.add(docente);
	}
	public boolean effettuaLogin(String email, String password) {
		for (Responsabile r : responsabili) {
			if (r.login(email, password)) {
				utenteLoggato = r;
				return true;
			}
		}
		for (Docente d : docenti) {
			if (d.login(email, password)) {
				utenteLoggato = d;
				return true;
			}
		}
		for (Studente s : studenti) {
			if (s.login(email, password)) {
				utenteLoggato = s;
				return true;
			}
		}
		throw new IllegalArgumentException("Errore. Email o password errati.");
	}

	public Utente getUtenteLoggato() {
		return utenteLoggato;
	}

	public int getNumeroInsegnamenti() {
		return insegnamenti.size();
	}

	public int getNumeroAule() {
		return aule.size();
	}

	public int getNumeroLezioni() {
		return lezioni.size();
	}

	public int getNumeroRichiesteInAttesa() {
		int count = 0;
		for (RichiestaSpostamento r : richieste) if (r.getStato().equals("In attesa")) count++;
		return count;
	}

	public ArrayList<Insegnamento> getInsegnamenti() {
		return insegnamenti;
	}

	public ArrayList<Aula> getAule() {

		return aule;
	}

	public ArrayList<Lezione> getTutteLezioni() {

		return lezioni;
	}

	public ArrayList<String> rilevaConflitti() {
		ArrayList<String> conflittiTrovati = new ArrayList<>();
		for (int i = 0; i < lezioni.size(); i++) {
			for (int j = i + 1; j < lezioni.size(); j++) {
				Lezione l1 = lezioni.get(i);
				Lezione l2 = lezioni.get(j);
				if (l1.getGiornoSettimana().equals(l2.getGiornoSettimana()) && l1.getOrainizio().equals(l2.getOrainizio())) {
					if (l1.getAula().getNome().equals(l2.getAula().getNome())) {
						conflittiTrovati.add(l1.getAula().getNome() + " occupata due volte " + l1.getGiornoSettimana() + " " + l1.getOrainizio());
					}
					if (l1.getInsegnamento().getDocente().getEmail().equals(l2.getInsegnamento().getDocente().getEmail())) {
						conflittiTrovati.add("Prof. " + l1.getInsegnamento().getDocente().getCognome() + " ha due lezioni sovrapposte " + l1.getGiornoSettimana() + " " + l1.getOrainizio());
					}
				}
			}
		}
		return conflittiTrovati;
	}

	public void setUtenteLoggato(Utente utente) {
		this.utenteLoggato = utente;
	}

	public void aggiungiRichiestaSpostamento(RichiestaSpostamento richiesta) {
		richiesteSpostamento.add(richiesta);
	}

	public ArrayList<RichiestaSpostamento> getRichiesteSpostamento() {
		return richiesteSpostamento;
	}

	public ArrayList<Lezione> getLezioniDelDocente(Docente prof) {
		ArrayList<Lezione> lezioniProf = new ArrayList<>();
		for (Lezione l : this.lezioni) {
			if (l.getInsegnamento().getDocente().getEmail().equals(prof.getEmail())) {
				lezioniProf.add(l);
			}
		}
		return lezioniProf;
	}
	public void aggiungiInsegnamento(Insegnamento i) {
		insegnamenti.add(i);
	}

	public void rimuoviInsegnamento(Insegnamento i) {
		insegnamenti.remove(i);
	}
	public ArrayList<Docente> getDocenti() {
		return docenti;
	}
	public void aggiungiAula(model.Aula a) {
		aule.add(a);
	}
	public ArrayList<model.Aula> getaule() {
		return aule;
	}
	public String[]getGiorniSettimana(){
		return new String[]{
				"Lunedi","Martedi","Mercoledi","Giovedi","Venerdi"
		};
	}
	public void eliminaRichiesta(int indice) {
		richiesteSpostamento.remove(indice);
	}
	public String accettaRichiesta(int indice) {
		RichiestaSpostamento req = richiesteSpostamento.get(indice);
		Lezione lezione = req.getLezionedaSpostare();
		String nuovaOraFine = req.getNuovaOraFine();
		String nuovoGiorno = req.getNuovoGiornoLezione();
		String nuovaOraInizio = req.getNuovaOraInizio();
		Docente docente = lezione.getInsegnamento().getDocente();
		Aula aula = lezione.getAula();
		for(Vincolo v:docente.getVincoli()){
			if(v.getVincoloGiornoSettimana().equalsIgnoreCase(nuovoGiorno)){
				if (isSovrapposto(nuovaOraInizio, nuovaOraFine, v.getVincoloOraInizio(), v.getVincoloOraFine())) {
					return "Impossibile approvare: il " + nuovoGiorno + " dalle " + nuovaOraInizio + " alle " + nuovaOraFine + " viola un vincolo di indisponibilità del Prof. " + docente.getCognome() + ".";
				}
			}
		}
		for (Lezione altraLezione : lezioni) {
			if (altraLezione == lezione) continue;
			if (altraLezione.getGiornoSettimana().equalsIgnoreCase(nuovoGiorno)) {
				if (isSovrapposto(nuovaOraInizio, nuovaOraFine, altraLezione.getOrainizio(), altraLezione.getOrafine())) {
					if (altraLezione.getAula().getNome().equalsIgnoreCase(aula.getNome())) {
						return "Impossibile approvare: l'aula " + aula.getNome() + " è già occupata da un'altra lezione (" + altraLezione.getInsegnamento().getNome() + ").";
					}
					if (altraLezione.getInsegnamento().getDocente().getCognome().equalsIgnoreCase(docente.getCognome())) {
						return "Impossibile approvare: il Prof. " + docente.getCognome() + " ha già un'altra lezione in contemporanea (" + altraLezione.getInsegnamento().getNome() + ").";
					}
				}
			}
		}
		lezione.setGiornoSettimana(nuovoGiorno);
		lezione.setOrainizio(nuovaOraInizio);
		lezione.setOrafine(nuovaOraFine);
		richiesteSpostamento.remove(indice);
		return "OK";
	}
	private boolean isSovrapposto(String inizio1, String fine1, String inizio2, String fine2) {
		return inizio1.compareTo(fine2) < 0 && fine1.compareTo(inizio2) > 0;
	}
}