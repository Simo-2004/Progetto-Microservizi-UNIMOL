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
GET  /api/v1/rooms/{ID_AULA}

#Aggiorna dati aula (capienza, dotazioni ecc...)
PUT  /api/v1/rooms/{ID_AULA}

#Elimina un'aula
DELETE  /api/v1/rooms/{ID_AULA}
</pre>

**Pianificazione delle lezioni (Amministrativi (input disponibilità da Docenti))**
<pre>
#Crea una nuova lezione
POST  /api/v1/lectures

#Lista di tutte le lezioni
GET  /api/v1/lectures

#Singola lezione
GET  /api/v1/lectures/{ID_LEZIONE}

#Aggiorna dati lezione (Orario, aula ecc...)
PUT  /api/v1/lectures/{ID_LEZIONE}

#Elimina lezione
DELETE  /api/v1/lectures/{ID_LEZIONE}

#Lista lezioni per un corso
GET  /api/v1/lectures/course/{ID_CORSO}

#Lezioni in una specifica aula
GET  /api/v1/lectures/room/{ID_AULA}

#Lezioni di un docente (in questo caso ID_UTENTE si riferisce all'id del professore)
GET  /api/v1/lectures/teacher/{ID_UTENTE}
</pre>


