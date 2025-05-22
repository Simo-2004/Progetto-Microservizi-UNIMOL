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

# Signature delle funzioni
**Gestione della disponibilità delle aule (Amministrativi)**
<pre> 
Room addRoom(Room newRoom); //newRoom = dati in input
List<Room> getAllRooms();
Room getRoomById(String roomId);
Room updateRoom(String roomId, Room updatedRoom); //updatedRoom = dati in input
void deleteRoom(String roomId);
</pre>

**Pianificazione delle lezioni (Amministrativi (input disponibilità da Docenti))**
<pre>
Lecture addLecture(Lecture newLecture); //newLecture = dati in input
List<Lecture> getAllLectures();
Lecture getLectureById(String lectureId);
Lecture updateLecture(String lectureId, Lecture updatedLecture); //updatedLecture = dati in input
void deleteLecture(String lectureId);
List<Lecture> getLecturesByCourseId(String courseId);
List<Lecture> getLecturesByRoomId(String roomId);
List<Lecture> getLecturesByTeacherId(String teacherId);
</pre>

**Preferenze orarie (Fornite a Amministrativi da Docenti)**
<pre>
Availability addTeacherAvailability(Availability availability); //availability = dati in input
List<Availability> getTeacherAvailability(String teacherId);
Availability updateTeacherAvailability(String availabilityId, Availability updatedAvailability); //updatedAvailability = dati in input
void deleteTeacherAvailability(String availabilityId);
</pre>

**Gestione conflitti (Amministrativi)**
<pre>
Conflict addConflict(Conflict newConflict);
List<Conflict> getAllConflicts();
Conflict getConflictById(String conflictId);
List<Conflict> getConflictsByRoomId(String roomId);
List<Conflict> getConflictsByTeacherId(String teacherId);
Conflict resolveConflict(String conflictId, Conflict updatedConflict);
</pre>


