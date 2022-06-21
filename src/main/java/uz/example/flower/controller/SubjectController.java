package uz.example.flower.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.example.flower.model.JSend;
import uz.example.flower.service.SubjectService;

import java.util.List;

@RestController
@RequestMapping("api/v1/subject")
public class SubjectController {
    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @PostMapping("save")
    public ResponseEntity<?> saveSubject(@RequestBody List<String> names) {
        JSend response = subjectService.saveSubject(names);
        return ResponseEntity.ok(response);
    }
}
