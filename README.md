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
- **Database**: MySQL
- **Message Broker**: RabbitMQ

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

# Signature delle funzioni (In Java)
**Gestione della disponibilità delle aule (Amministrativi)**
```java
DA FARE
REST (GET,POST ecc...) con il metodo java e ad esempio example/people
Room addRoom(Room newRoom); //newRoom = dati in input
Room getRoomById(String roomId);
Room updateRoom(String roomId, Room updatedRoom); //updatedRoom = dati in input
List <Room> getAllRooms();
void deleteRoom(String roomId);
```

**Pianificazione delle lezioni (Amministrativi (input disponibilità da Docenti))**
```java
Lecture addLecture(Lecture newLecture); //newLecture = dati in input
Lecture getLectureById(String lectureId);
Lecture updateLecture(String lectureId, Lecture updatedLecture); //updatedLecture = dati in input
void deleteLecture(String lectureId);
List <Lecture> getAllLectures();
List <Lecture> getLecturesByCourseId(String courseId);
List <Lecture> getLecturesByRoomId(String roomId);
List <Lecture> getLecturesByTeacherId(String teacherId);
```

**Preferenze orarie (Fornite a Amministrativi da Docenti)**
```java
Availability addTeacherAvailability(Availability availability); //availability = dati in input
List<Availability> getTeacherAvailability(String teacherId);
Availability updateTeacherAvailability(String availabilityId, Availability updatedAvailability); //updatedAvailability = dati in input
void deleteTeacherAvailability(String availabilityId);
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


