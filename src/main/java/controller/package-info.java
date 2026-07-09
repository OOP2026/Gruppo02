/**
 * <h1>Capitolo 5: Features e Controlli - CONTROLLER</h1>
 * <p>Questo package contiene il Controller, il quale agisce come gestore dello stato
 * applicativo e come garante assoluto dell'integrità dei dati (Pattern MVC).</p>
 * * <h2>Responsabilità principali:</h2>
 * <ul>
 * <li><b>Autenticazione:</b> Verifica la validità delle credenziali.</li>
 * <li><b>Prevenzione Sovrapposizioni:</b> Impedisce l'inserimento di lezioni nella stessa aula o per lo stesso docente in orari coincidenti.</li>
 * <li><b>Gestione Vincoli:</b> Controlla che i docenti non superino il limite dei vincoli orari imposti.</li>
 * </ul>
 */
package controller;