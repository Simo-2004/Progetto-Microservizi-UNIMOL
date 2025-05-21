# Microservizio Pianificazione orale e aule
# Introduzione
Il microservizio è responsabile della pianificazione e gestione della disponibilità delle aule e pianificazione degli orari. Inoltre vengono gestite le seguenti funzionalità:
- (**Amministrativi**) Gestione della disponibilità delle aule
- (**Amministrativi**) Pianificazione degli orari e delle lezioni
- (**Amministrativi**) Pianificazione delle sessioni d’esame
- (**Amministrativi**) Gestione dei conflitti di orario
- (**Amministrativi**) Forniscono informazioni su aule e orari (creano il file con gli orari, date di esami ecc...)

# Tecnologie utilizzate
- **Documentazione**: Swagger
- **Framework**: SpringBoot
- **Database**: PostgreSQL

# Entità del database
Tabelle individuate con i relativi attributi
- Utente (Amministrativo o docente)
  - `ID_UTENTE` (Chiave primaria)
  - `Nome`
  - `Cognome`
  - `Email`
  - `Ruolo` (ENUM: "Amministrativo", "Docente")
 
- Aula
  - `ID_AULA` (Chiave primaria)
  - `Nome`
  - `Edificio`
  - `Capienza`
  - `Disponibile` (Booleano)
  - `Dotazioni` (Es. Computer, proiettori)
 
- Corso
  - `ID_CORSO` (Chiave primaria)
  - `Nome`
  - `Codice`
  - `ID_DOCENTE` (Chiave esterna per la tabella "Utente")
  - `Anno_accademico`
  - `Semestre`
 
- Lezione
  - `ID_LEZIONE` (Chiave primaria)
  - `ID_CORSO` (Chiave esterna per la tabella "Corso")
  - `ID_AULA` (chiave esterna per la tabella "Aula")
  - `Data`
  - `Ora_inizio`
  - `Ora_fine`
 
- Esame
  - `ID_ESAME` (Chiave primaria)
  - `ID_CORSO` (Chiave esterna per la tabella "Corso")
  - `ID_AULA` (chiave esterna per la tabella "Aula")
  - `Data`
  - `Ora_inizio`
  - `Ora_fine`
  - `Tipo` (ENUM: "Scritto", "Orale", "Altro")

# API REST
**Gestione della disponibilità delle aule (Amministrativi)**
<pre> 
#Crea una nuova aula
POST  /api/v1/rooms

#Lista di tutte le aule
GET  /api/v1/rooms

#Dettaglio di una singola aula
GET  /api/v1/rooms/{id}

#Aggiorna dati aula (capienza, dotazioni, ecc.)
PUT  /api/v1/rooms/{id}

#Elimina un'aula (solo se non collegata a eventi attivi)
DELETE  /api/v1/rooms/{id}
</pre>
