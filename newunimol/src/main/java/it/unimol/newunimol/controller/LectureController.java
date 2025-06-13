package it.unimol.newunimol.controller;

import it.unimol.newunimol.model.Lecture;
import it.unimol.newunimol.service.LectureService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import it.unimol.newunimol.service.TokenJWTService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lectures")
public class LectureController {

    @Autowired
    private LectureService lectureService;

    @Autowired
    private TokenJWTService tokenJWTService;

    @PostMapping("/create_lecture")
    public ResponseEntity<Lecture> addLecture(@RequestBody Lecture newLecture, HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.replace("Bearer ", "");
        String role = tokenJWTService.extractRole(token);

        if("ADMIN".equals(role) || "SADMIN".equals(role)) {
            return ResponseEntity.ok(lectureService.addLecture(newLecture));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/find_lecture/{id}")
    public ResponseEntity<Lecture> getLectureById(@PathVariable String id) {

        if(lectureService.getLectureById(id) == null)
        {
            return ResponseEntity.notFound().build();
        }
        else
        {
            return ResponseEntity.ok(lectureService.getLectureById(id));
        }


    }

    @PutMapping("/update_lecture/{id}")
    public ResponseEntity<Lecture> updateLecture(@PathVariable String id, @RequestBody Lecture updatedLecture, HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.replace("Bearer ", "");
        String role = tokenJWTService.extractRole(token);

        if("ADMIN".equals(role) || "SADMIN".equals(role)) {
            return ResponseEntity.ok(lectureService.updateLecture(id, updatedLecture));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @DeleteMapping("/delete_lecture/{id}")
    public ResponseEntity<?> deleteLecture(@PathVariable String id, HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.replace("Bearer ", "");
        String role = tokenJWTService.extractRole(token);

        if("ADMIN".equals(role) || "SADMIN".equals(role)) {
            lectureService.deleteLecture(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/all_lectures")
    public ResponseEntity<List<Lecture>> getAllLectures() {
        return ResponseEntity.ok(lectureService.getAllLectures());
    }
}