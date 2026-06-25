CREATE TABLE Utente (
    email VARCHAR(100) PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    cognome VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    ruolo VARCHAR(20) NOT NULL 
);


CREATE TABLE Studente (
    matricola VARCHAR(20) PRIMARY KEY,
    email VARCHAR(100) REFERENCES Utente(email) ON DELETE CASCADE,
    anno_corso INT NOT NULL
);


CREATE TABLE Vincolo (
    id SERIAL PRIMARY KEY,
    docente_email VARCHAR(100) REFERENCES Utente(email) ON DELETE CASCADE,
    giorno_settimana VARCHAR(20) NOT NULL,
    ora_inizio VARCHAR(10) NOT NULL,
    ora_fine VARCHAR(10) NOT NULL
);

CREATE TABLE Aula (
    nome VARCHAR(50) PRIMARY KEY
);


CREATE TABLE Insegnamento (
    nome VARCHAR(100) PRIMARY KEY,
    cfu INT NOT NULL,
    anno_corso INT NOT NULL,
    docente_email VARCHAR(100) REFERENCES Utente(email) ON DELETE SET NULL
);


CREATE TABLE Lezione (
    id SERIAL PRIMARY KEY,
    insegnamento_nome VARCHAR(100) REFERENCES Insegnamento(nome) ON DELETE CASCADE,
    giorno_settimana VARCHAR(20) NOT NULL,
    ora_inizio VARCHAR(10) NOT NULL,
    ora_fine VARCHAR(10) NOT NULL,
    aula_nome VARCHAR(50) REFERENCES Aula(nome) ON DELETE SET NULL
);


CREATE TABLE RichiestaSpostamento (
    id SERIAL PRIMARY KEY,
    lezione_id INT REFERENCES Lezione(id) ON DELETE CASCADE,
    nuovo_giorno VARCHAR(20) NOT NULL,
    nuova_ora_inizio VARCHAR(10) NOT NULL,
    nuova_ora_fine VARCHAR(10) NOT NULL,
    motivazione TEXT,
    stato VARCHAR(20) DEFAULT 'In attesa'
);