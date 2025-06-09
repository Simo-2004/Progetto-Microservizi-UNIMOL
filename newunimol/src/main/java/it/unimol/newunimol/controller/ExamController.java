package it.unimol.newunimol.controller;

import it.unimol.newunimol.model.Exam;
import it.unimol.newunimol.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    @PostMapping("/create_exam")
    public Exam addExam(@RequestBody Exam newExam) {
        return examService.addExam(newExam);
    }

    @GetMapping("/find_exam/{id}")
    public Exam getExamById(@PathVariable String id) {
        return examService.getExamById(id);
    }

    @PutMapping("/update_exam/{id}")
    public Exam updateExam(@PathVariable String id, @RequestBody Exam updatedExam) {
        return examService.updateExam(id, updatedExam);
    }

    @DeleteMapping("/delete_exam/{id}")
    public void deleteExam(@PathVariable String id) {
        examService.deleteExam(id);
    }

    @GetMapping("/all_exams")
    public List<Exam> getAllExams() {
        return examService.getAllExams();
    }
}