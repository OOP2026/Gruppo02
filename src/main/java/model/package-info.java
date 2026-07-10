/**
 * <h1>Architettura dei Package - MODEL</h1>
 * <p>Il package model rappresenta il cuore logico dell'applicazione. Qui sono state
 * implementate le classi che mappano le entità reali dell'università, avvalendosi dei
 * concetti di ereditarietà e polimorfismo.</p>
 * Qui sotto puoi vedere il Diagramma delle Classi del nostro sistema:</p>
 *  * * * <img src="doc-files/diagramma_uml.png" alt="Diagramma delle Classi del Model" style="max-width: 100%;">
 * * <h2>Gestione delle Utenze e dei Ruoli</h2>

 * <ul>
 * <li><b>Utente:</b> Classe base che incapsula i concetti fondamentali di autenticazione.</li>
 * <li><b>Responsabile:</b> Gestisce le anagrafiche, alloca le lezioni nelle aule e accetta le richieste.</li>
 * <li><b>Docente:</b> Personale didattico. Possiede vincoli di orario e può richiedere spostamenti.</li>
 * <li><b>Studente:</b> Utente base che visualizza l'orario relativo al proprio anno di corso.</li>
 * </ul>
 * * <h2>Processi e Struttura</h2>
 * <p>Il sistema gestisce <b>Lezioni</b>, <b>Insegnamenti</b> e <b>Aule</b>, impedendo
 * sovrapposizioni temporali nell'allocazione delle risorse.</p>
 *
 ** <p>Il codice sorgente è stato suddiviso in package per rispettare i principi di modularità
 *  * e l'architettura MVC (Model-View-Controller). Qui sotto è illustrato il Diagramma dei Package:</p>
 *  * * * <img src="doc-files/diagramma_di_dettaglio.jpg" alt="Diagramma dei Package" style="max-width: 80%; border: 1px solid black;">
 *  * * <h2>Analisi dei Componenti Logici</h2>
 *  * <ul>
 *  * <li><b>model:</b> Rappresenta il cuore logico dell'applicazione e le entità del dominio.</li>
 *  * <li><b>controller:</b> Gestisce il flusso dell'applicazione e fa da ponte tra GUI e Database.</li>
 *  * <li><b>gui:</b> Contiene le interfacce grafiche con cui interagisce l'utente.</li>
 *  * <li><b>dao e database:</b> Gestiscono la connessione e le query verso il database PostgreSQL.</li>
 *  * </ul>
 *  */

package model;
