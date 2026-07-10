/**
 * <h1> Architettura dei Package - DATABASE</h1>
 * <p>Questo package ha la responsabilità esclusiva di gestire la connessione fisica
 * al database relazionale (PostgreSQL).</p>
 * * <p>Isola la logica di connessione dal resto dell'applicazione, garantendo che le
 * credenziali e i driver del database siano centralizzati in un unico punto,
 * facilitando così la manutenzione e la sicurezza del sistema.</p>
 */
package database;