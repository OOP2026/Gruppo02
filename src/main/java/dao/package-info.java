/**
 * <h1> Architettura dei Package - DAO (Data Access Object)</h1>
 * <p>Questo package contiene esclusivamente le <b>Interfacce</b> relative al pattern DAO.
 * Definisce i contratti per tutte le operazioni di tipo CRUD (Create, Read, Update, Delete)
 * che l'applicazione può effettuare sui dati persistenti.</p>
 * * <h2>Scopo Architetturale</h2>
 * <p>Mantiene il livello del Controller completamente all'oscuro della tecnologia di database
 * utilizzata sottostante (ad esempio PostgreSQL o MySQL), garantendo un alto livello di astrazione.</p>
 */
package dao;