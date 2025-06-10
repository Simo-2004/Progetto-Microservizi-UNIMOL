package it.unimol.newunimol.controller;

import it.unimol.newunimol.model.Exam;
import it.unimol.newunimol.service.ExamService;
import it.unimol.newunimol.service.TokenJWTService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    @Autowired
    private TokenJWTService tokenJWTService;

    @PostMapping("/create_exam")
    public ResponseEntity<Exam> addExam(@RequestBody Exam newExam, HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.replace("Bearer ", "");
        String role = tokenJWTService.extractRole(token);

        if("ADMIN".equals(role) || "SADMIN".equals(role)) {
            return ResponseEntity.ok(examService.addExam(newExam));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/find_exam/{id}")
    public ResponseEntity<Exam> getExamById(@PathVariable String id, HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.replace("Bearer ", "");
        String role = tokenJWTService.extractRole(token);

        if("ADMIN".equals(role) || "SADMIN".equals(role)) {
            return ResponseEntity.ok(examService.getExamById(id));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PutMapping("/update_exam/{id}")
    public ResponseEntity<Exam> updateExam(@PathVariable String id, @RequestBody Exam updatedExam, HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.replace("Bearer ", "");
        String role = tokenJWTService.extractRole(token);

        if("ADMIN".equals(role) || "SADMIN".equals(role)) {
            return ResponseEntity.ok(examService.updateExam(id, updatedExam));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @DeleteMapping("/delete_exam/{id}")
    public ResponseEntity<?> deleteExam(@PathVariable String id, HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.replace("Bearer ", "");
        String role = tokenJWTService.extractRole(token);

        if("ADMIN".equals(role) || "SADMIN".equals(role)) {
            examService.deleteExam(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/all_exams")
    public ResponseEntity<List<Exam>> getAllExams(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.replace("Bearer ", "");
        String role = tokenJWTService.extractRole(token);

        if("ADMIN".equals(role) || "SADMIN".equals(role)) {
            return ResponseEntity.ok(examService.getAllExams());
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}