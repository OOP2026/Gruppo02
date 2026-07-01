-- ==========================================
-- 0. PULIZIA DELLE VECCHIE TABELLE (Piazza pulita!)
-- ==========================================
DROP TABLE IF EXISTS RichiestaSpostamento CASCADE;
DROP TABLE IF EXISTS Lezione CASCADE;
DROP TABLE IF EXISTS Vincolo CASCADE;
DROP TABLE IF EXISTS Insegnamento CASCADE;
DROP TABLE IF EXISTS Aula CASCADE;
DROP TABLE IF EXISTS Studente CASCADE;
DROP TABLE IF EXISTS Utente CASCADE;

-- ==========================================
-- 1. CREAZIONE TABELLE UTENTI
-- ==========================================
CREATE TABLE Utente (
    email VARCHAR(255) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cognome VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    ruolo VARCHAR(50) NOT NULL
);

CREATE TABLE Studente (
    matricola VARCHAR(50) PRIMARY KEY,
    email VARCHAR(255) REFERENCES Utente(email) ON DELETE CASCADE,
    anno_corso INT NOT NULL
);

-- ==========================================
-- 2. CREAZIONE AULE E INSEGNAMENTI
-- ==========================================
CREATE TABLE Aula (
    nome VARCHAR(50) PRIMARY KEY
);

CREATE TABLE Insegnamento (
    nome VARCHAR(255) PRIMARY KEY,
    cfu INT NOT NULL,
    anno_corso INT NOT NULL,
    docente_email VARCHAR(255) REFERENCES Utente(email) ON DELETE CASCADE
);

-- ==========================================
-- 3. CREAZIONE LEZIONI, VINCOLI E RICHIESTE
-- ==========================================
CREATE TABLE Lezione (
    insegnamento_nome VARCHAR(255) REFERENCES Insegnamento(nome) ON DELETE CASCADE,
    giorno_settimana VARCHAR(20) NOT NULL,
    ora_inizio VARCHAR(5) NOT NULL,
    ora_fine VARCHAR(5) NOT NULL,
    aula_nome VARCHAR(50) REFERENCES Aula(nome) ON DELETE CASCADE,
    PRIMARY KEY (insegnamento_nome, giorno_settimana, ora_inizio) 
);

CREATE TABLE Vincolo (
    giorno_settimana VARCHAR(20) NOT NULL,
    ora_inizio VARCHAR(5) NOT NULL,
    ora_fine VARCHAR(5) NOT NULL,
    docente_email VARCHAR(255) REFERENCES Utente(email) ON DELETE CASCADE,
    PRIMARY KEY (docente_email, giorno_settimana, ora_inizio, ora_fine)
);

CREATE TABLE RichiestaSpostamento (
    insegnamento_nome VARCHAR(255) REFERENCES Insegnamento(nome) ON DELETE CASCADE,
    vecchio_giorno VARCHAR(20) NOT NULL,
    nuovo_giorno VARCHAR(20) NOT NULL,
    nuova_ora_inizio VARCHAR(5) NOT NULL,
    nuova_ora_fine VARCHAR(5) NOT NULL,
    motivazione TEXT,
    stato VARCHAR(50) DEFAULT 'In attesa',
    PRIMARY KEY (insegnamento_nome, vecchio_giorno, nuovo_giorno)
);

-- ==========================================
-- 4. INSERIMENTO DEI DATI DI BASE
-- ==========================================
INSERT INTO Utente (email, nome, cognome, password, ruolo) 
VALUES ('admin@unina.it', 'Admin', 'Responsabile', 'admin', 'RESPONSABILE');

INSERT INTO Aula (nome) VALUES ('A1'), ('A2'), ('B1'), ('B2'), ('Laboratorio Info');