# Microservizio Pianificazione orale e aule
# Introduzione
Il microservizio è responsabile della pianificazione e gestione della disponibilità delle aule e pianificazione degli orari. Inoltre vengono gestite le seguenti funzionalità:
- (**Amministrativi**) Gestione della disponibilità delle aule
- (**Amministrativi**) Pianificazione degli orari e delle lezioni (input disponibilità da Docenti)
- (**Amministrativi**) Pianificazione delle sessioni d’esame (input disponibilità da Docenti)
- (**Amministrativi**) Gestione dei conflitti di orario
- (**Amministrativi**) Forniscono informazioni su aule e orari (creano il file con gli orari, date di esami ecc...)

# Tecnologie utilizzate
- **Documentazione API**: Swagger
- **Framework**: SpringBoot
- **Database**: MySQL
- **Message Broker**: RabbitMQ

# Entità del database
Tabelle individuate con i relativi attributi
- Utente (Amministrativo o docente)
  - `idUtente` (Chiave primaria)
  - `Nome`
  - `Cognome`
  - `Email`
  - `Ruolo` (ENUM: "Amministrativo", "Docente")
 
- Aula
  - `idAula` (Chiave primaria)
  - `Nome`
  - `Edificio`
  - `Capienza`
  - `Disponibile` (Booleano)
  - `Dotazioni` (Es. Computer, proiettori)
 
- Corso
  - `idCorso` (Chiave primaria)
  - `Nome`
  - `Codice`
  - `idDocente` (Chiave esterna per la tabella "Utente")
  - `Anno_accademico`
  - `Semestre`
 
- Lezione
  - `idLezione` (Chiave primaria)
  - `idCorso` (Chiave esterna per la tabella "Corso")
  - `idAula` (chiave esterna per la tabella "Aula")
  - `Data`
  - `Ora_inizio`
  - `Ora_fine`
 
- Esame
  - `idEsame` (Chiave primaria)
  - `idCorso` (Chiave esterna per la tabella "Corso")
  - `idAula` (chiave esterna per la tabella "Aula")
  - `Data`
  - `Ora_inizio`
  - `Ora_fine`
  - `Tipo` (ENUM: "Scritto", "Orale", "Altro")

# API REST
**Gestione delle aule (Amministrativi)**
```bash
CREAZIONE AULA
POST  /api/v1/rooms/create_room

Input:
{
  idAula: string
  nome: string
  edificio: string
  capienza: int
  disponibile: boolean
  dotazioni: string
}

Output:
{
  idAula: string
}
```
```bash
LISTA DI TUTTE LE AULE
GET  /api/v1/rooms/all_room

Input:
{
  
}

Output:
{
  idAula: string
  nome: string
  edificio: string
  capienza: int
  disponibile: boolean
  dotazioni: string
}
```

```bash
VISUALIZZAZIONE AULA PER ID
GET  /api/v1/rooms/find_room/{id}

Input:
{
  
}

Output:
{
  idAula: string
  nome: string
  edificio: string
  capienza: int
  disponibile: boolean
  dotazioni: string
}
```

```bash
MODIFICA AULA PER ID
PUT /api/v1/rooms/update_room/{id}

Input:
{
  nome: string
  edificio: string
  capienza: int
  disponibile: boolean
  dotazioni: string
}

Output:
{
  idAula: string
}
```

```bash
ELIMINA AULA PER ID
DELETE /api/v1/rooms/delete_room/{id}

Input:
{
  
}

Output:
{
  
}
```

**Pianificazione delle lezioni e orari (Amministrativi (input disponibilità da Docenti))**
```bash
CREAZIONE LEZIONI
POST  /api/v1/lectures/create_lecture

Input:
{
  "idLezione": "string",
  "nomeLezione": "string",
  "docenteId": "string",
  "idAula": "string",
  "data": "2025-05-25",
  "oraInizio": "09:00",
  "oraFine": "13:00"
}

Output:
{
  "idLezione": "string",
  "nomeLezione": "string",
  "docenteId": "string",
  "idAula": "string",
  "data": "2025-05-25",
  "oraInizio": "09:00",
  "oraFine": "13:00"
}
```

```bash
VISUALIZZAZIONE LEZIONE PER ID
GET  /api/v1/lectures/find_lecture/{id}

Input:
{
  
}

Output:
{
  "idLezione": "string",
  "nomeLezione": "string",
  "docenteId": "string",
  "idAula": "string",
  "data": "2025-05-25",
  "oraInizio": "09:00",
  "oraFine": "13:00"
}
```

```bash
LISTA DI TUTTE LE LEZIONI
GET  /api/v1/lectures/all_lectures

Input:
{
  
}

Output:
{
  "idLezione": "string",
  "nomeLezione": "string",
  "docenteId": "string",
  "idAula": "string",
  "data": "2025-05-25",
  "oraInizio": "09:00",
  "oraFine": "13:00"
}
```

```bash
MODIFICA LEZIONI
PUT  /api/v1/lectures/update_lecture/{id}

Input:
{
  
}

Output:
{
  "idLezione": "string",
  "nomeLezione": "string",
  "docenteId": "string",
  "idAula": "string",
  "data": "2025-05-25",
  "oraInizio": "09:00",
  "oraFine": "13:00"
}
```

```bash
ELIMINAZIONE LEZIONE
DELETE  /api/v1/lectures/delete_lecture/{id}

Input:
{
  
}

Output:
{
  
}
```

**Pianificazioni sessione d'esame**
```java
Exam addExam(Exam exam);

```

**Preferenze orarie (Fornite a Amministrativi da Docenti)**
```bash
CREAZIONE DISPONIBILITÀ
POST  /api/v1/availability/create_availability

Input:
{
  "idUtente": "string",
  "disponibile": true,
  "nome_utente": "string",
  "cognome_utente": "string",
  "data": "2025-05-25",
  "ora_inizio": "09:00",
  "ora_fine": "13:00"
}

Output:
{
  "idUtente": "string",
  "disponibile": true,
  "nome_utente": "string",
  "cognome_utente": "string",
  "data": "2025-05-25",
  "ora_inizio": "09:00",
  "ora_fine": "13:00"
}
```
```bash
LISTA DI TUTTE LE DISPONIBILITÀ
GET  /api/v1/availability/all_availability

Input:
{
  
}

Output:
{
  "idUtente": "string",
  "disponibile": true,
  "nome_utente": "string",
  "cognome_utente": "string",
  "data": "2025-05-25",
  "ora_inizio": "09:00",
  "ora_fine": "13:00"
}
```

```bash
VISUALIZZAZIONE DISPONIBILITÀ PER ID
GET  /api/v1/availability/find_availability/{idUtente}

Input:
{
  
}

Output:
{
  "idUtente": "string",
  "disponibile": true,
  "nome_utente": "string",
  "cognome_utente": "string",
  "data": "2025-05-25",
  "ora_inizio": "09:00",
  "ora_fine": "13:00"
}
```

```bash
MODIFICA DISPONIBILITÀ PER ID
PUT /api/v1/availability/update_availability/{idUtente}

Input:
{
  "idUtente": "string",
  "disponibile": true,
  "nome_utente": "string",
  "cognome_utente": "string",
  "data": "2025-05-25",
  "ora_inizio": "09:00",
  "ora_fine": "13:00"
}

Output:
{
  "idUtente": "string",
  "disponibile": true,
  "nome_utente": "string",
  "cognome_utente": "string",
  "data": "2025-05-25",
  "ora_inizio": "09:00",
  "ora_fine": "13:00"
}
```

```bash
ELIMINAZIONE DISPONIBILITÀ PER ID
DELETE  /api/v1/availability/delete_availability/{idUtente}

Input:
{
  
}

Output:
{
  "idUtente": "string",
  "disponibile": true,
  "nome_utente": "string",
  "cognome_utente": "string",
  "data": "2025-05-25",
  "ora_inizio": "09:00",
  "ora_fine": "13:00"
}
```

**Gestione conflitti (Amministrativi)**
```java
Conflict addConflict(Conflict newConflict);
List<Conflict> getAllConflicts();
Conflict getConflictById(String conflictId);
List<Conflict> getConflictsByRoomId(String roomId);
List<Conflict> getConflictsByTeacherId(String teacherId);
Conflict resolveConflict(String conflictId, Conflict updatedConflict);
```


