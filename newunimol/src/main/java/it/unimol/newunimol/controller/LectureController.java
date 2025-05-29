package it.unimol.newunimol.controller;

import it.unimol.newunimol.model.Lecture;
import it.unimol.newunimol.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lectures")
public class LectureController {

    @Autowired
    private LectureService lectureService;

    @PostMapping("/create_lecture")
    public Lecture addLecture(@RequestBody Lecture newLecture) {
        return lectureService.addLecture(newLecture);
    }

    @GetMapping("/find_lecture/{id}")
    public Lecture getLectureById(@PathVariable String id) {
        return lectureService.getLectureById(id);
    }

    @PutMapping("/update_lecture/{id}")
    public Lecture updateLecture(@PathVariable String id, @RequestBody Lecture updatedLecture) {
        return lectureService.updateLecture(id, updatedLecture);
    }

    @DeleteMapping("/delete_lecture/{id}")
    public void deleteLecture(@PathVariable String id) {
        lectureService.deleteLecture(id);
    }

    @GetMapping("/all_lectures")
    public List<Lecture> getAllLectures() {
        return lectureService.getAllLectures();
    }
}