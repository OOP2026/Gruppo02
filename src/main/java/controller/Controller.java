package controller;

import dao.*;
import implementazionedao.*;
import model.*;
import java.util.ArrayList;

public class Controller {

	private UtenteDAO utenteDAO = new UtentePostgresDAO();
	private LezioneDAO lezioneDAO = new LezionePostgresDAO();
	private AulaDAO aulaDAO = new AulaPostgresDAO();
	private InsegnamentoDAO insegnamentoDAO = new InsegnamentoPostgresDAO();
	private VincoloDAO vincoloDAO = new VincoloPostgresDAO();
	private RichiestaSpostamentoDAO richiestaDAO = new RichiestaSpostamentoPostgresDAO();

	private ArrayList<Studente> studenti = new ArrayList<>();
	private ArrayList<Docente> docenti = new ArrayList<>();
	private ArrayList<Responsabile> responsabili = new ArrayList<>();
	private Utente utenteLoggato = null;

	public Controller() {}

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
	public ArrayList<Docente> getDocenti() { return docenti; }

	public ArrayList<Lezione> getTutteLezioni() { return lezioneDAO.getTutteLezioni(); }
	public int getNumeroLezioni() { return lezioneDAO.getTutteLezioni().size(); }
	public ArrayList<Lezione> getLezioniDelDocente(Docente prof) { return lezioneDAO.getLezioniDelDocente(prof.getEmail()); }
	/**
	 * Tenta di creare e inserire una nuova lezione nel sistema.
	 * * @param nuovaLezione L'oggetto Lezione contenente orario, aula e insegnamento.
	 * @return true se la lezione è stata inserita con successo, false altrimenti.
	 * * @implNote <b>Flusso di esecuzione (Sequence):</b>
	 * <ol>
	 * <li>Verifica se il Docente ha un Vincolo che blocca questo giorno/orario.</li>
	 * <li>Richiama {@code rilevaConflitti()} per controllare se l'Aula è già occupata.</li>
	 * <li>Richiama {@code rilevaConflitti()} per controllare se il Docente sta già insegnando.</li>
	 * <li>Se tutti i controlli passano, invia la lezione al DAO per il salvataggio nel Database.</li>
	 * </ol>
	 */
	public boolean creaLezione(Lezione nuovaLezione) {
		Docente docente = nuovaLezione.getInsegnamento().getDocente();
		for (Vincolo v : vincoloDAO.getVincoliPerDocente(docente.getEmail())) {
			if (v.getVincoloGiornoSettimana().equalsIgnoreCase(nuovaLezione.getGiornoSettimana())) {
				if (isSovrapposto(nuovaLezione.getOrainizio(), nuovaLezione.getOrafine(), v.getVincoloOraInizio(), v.getVincoloOraFine())) {
					return false;
				}
			}
		}
		if (rilevaConflitti(nuovaLezione)) { return false; }
		return lezioneDAO.inserisciLezione(nuovaLezione);
	}

	public void eliminaLezione(Lezione lezione) { lezioneDAO.eliminaLezione(lezione); }

	public ArrayList<Aula> getAule() { return aulaDAO.getTutteLeAule(); }
	public int getNumeroAule() { return aulaDAO.getTutteLeAule().size(); }
	public void aggiungiAula(Aula a) { aulaDAO.inserisciAula(a); }
	public void rimuoviAula(Aula a) { aulaDAO.eliminaAula(a); }

	public ArrayList<Insegnamento> getInsegnamenti() { return insegnamentoDAO.getTuttiInsegnamenti(); }
	public int getNumeroInsegnamenti() { return insegnamentoDAO.getTuttiInsegnamenti().size(); }
	public void aggiungiInsegnamento(Insegnamento i) { insegnamentoDAO.inserisciInsegnamento(i); }
	public void rimuoviInsegnamento(Insegnamento i) { insegnamentoDAO.eliminaInsegnamento(i); }

	public ArrayList<Vincolo> getVincoliDocente(Docente docente) { return vincoloDAO.getVincoliPerDocente(docente.getEmail()); }
	public void aggiungiVincolo(Docente docente, Vincolo v) { vincoloDAO.inserisciVincolo(v, docente.getEmail()); }
	public void rimuoviVincolo(Vincolo v) {
		if (utenteLoggato != null) { vincoloDAO.eliminaVincolo(v, utenteLoggato.getEmail()); }
	}

	public void aggiungiRichiestaSpostamento(RichiestaSpostamento richiesta) { richiestaDAO.inserisciRichiesta(richiesta); }
	public ArrayList<RichiestaSpostamento> getRichiesteSpostamento() { return richiestaDAO.getTutteLeRichieste(); }
	public int getNumeroRichiesteInAttesa() {
		int count = 0;
		for (RichiestaSpostamento r : richiestaDAO.getTutteLeRichieste()) {
			if ("In attesa".equals(r.getStato())) count++;
		}
		return count;
	}

	public void eliminaRichiesta(int indice) {
		ArrayList<RichiestaSpostamento> richieste = richiestaDAO.getTutteLeRichieste();
		if (indice >= 0 && indice < richieste.size()) {
			RichiestaSpostamento req = richieste.get(indice);
			richiestaDAO.eliminaRichiesta(req);
		}
	}

	public String accettaRichiesta(int indice) {
		ArrayList<RichiestaSpostamento> richieste = richiestaDAO.getTutteLeRichieste();
		if (indice < 0 || indice >= richieste.size()) return "Errore indice";

		RichiestaSpostamento req = richieste.get(indice);
		Lezione lezione = req.getLezionedaSpostare();
		Docente docente = lezione.getInsegnamento().getDocente();

		for(Vincolo v : vincoloDAO.getVincoliPerDocente(docente.getEmail())){
			if(v.getVincoloGiornoSettimana().equalsIgnoreCase(req.getNuovoGiornoLezione())){
				if (isSovrapposto(req.getNuovaOraInizio(), req.getNuovaOraFine(), v.getVincoloOraInizio(), v.getVincoloOraFine())) {
					return "Impossibile approvare: viola un vincolo del Prof. " + docente.getCognome();
				}
			}
		}

		for (Lezione altraLezione : lezioneDAO.getTutteLezioni()) {
			if (altraLezione.getInsegnamento().getNome().equals(lezione.getInsegnamento().getNome())) continue;
			if (altraLezione.getGiornoSettimana().equalsIgnoreCase(req.getNuovoGiornoLezione())) {
				if (isSovrapposto(req.getNuovaOraInizio(), req.getNuovaOraFine(), altraLezione.getOrainizio(), altraLezione.getOrafine())) {
					return "Conflitto rilevato con un'altra lezione.";
				}
			}
		}

		lezioneDAO.eliminaLezione(lezione);
		lezione.setGiornoSettimana(req.getNuovoGiornoLezione());
		lezione.setOrainizio(req.getNuovaOraInizio());
		lezione.setOrafine(req.getNuovaOraFine());
		lezioneDAO.inserisciLezione(lezione);
		richiestaDAO.eliminaRichiesta(req);

		return "OK";
	}

	public boolean rilevaConflitti(Lezione nuovaLezione) {
		for (Lezione l : lezioneDAO.getTutteLezioni()) {
			if (l.getGiornoSettimana().equalsIgnoreCase(nuovaLezione.getGiornoSettimana()) &&
					l.getOrainizio().equals(nuovaLezione.getOrainizio())) {
				if (l.getAula().getNome().equalsIgnoreCase(nuovaLezione.getAula().getNome())) { return true; }
				if (l.getInsegnamento().getDocente().getEmail().equals(nuovaLezione.getInsegnamento().getDocente().getEmail())) { return true; }
			}
		}
		return false;
	}

	public ArrayList<String> rilevaConflitti() {
		ArrayList<String> conflitti = new ArrayList<>();
		ArrayList<Lezione> lezioni = lezioneDAO.getTutteLezioni();
		for (int i = 0; i < lezioni.size(); i++) {
			for (int j = i + 1; j < lezioni.size(); j++) {
				Lezione l1 = lezioni.get(i);
				Lezione l2 = lezioni.get(j);
				if (l1.getGiornoSettimana().equals(l2.getGiornoSettimana()) && l1.getOrainizio().equals(l2.getOrainizio())) {
					if (l1.getAula().getNome().equals(l2.getAula().getNome())) {
						conflitti.add(l1.getAula().getNome() + " occupata il " + l1.getGiornoSettimana() + " alle " + l1.getOrainizio());
					}
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

	// --- NUOVI METODI CENTRALIZZATI PER ELIMINARE LA DUPLICAZIONE ---
	public int getColonnaGiorno(String giorno) {
		if (giorno == null) return -1;
		switch (giorno.toLowerCase()) {
			case "lunedì": case "lunedi": return 1;
			case "martedì": case "martedi": return 2;
			case "mercoledì": case "mercoledi": return 3;
			case "giovedì": case "giovedi": return 4;
			case "venerdì": case "venerdi": return 5;
			default: return -1;
		}
	}

	public int getRigaOrario(String oraInizio, String[] fasceOrarie) {
		if (oraInizio == null || fasceOrarie == null) return -1;
		for (int i = 0; i < fasceOrarie.length; i++) {
			if (oraInizio.startsWith(fasceOrarie[i].substring(0, 2))) { return i; }
		}
		return -1;
	}

}