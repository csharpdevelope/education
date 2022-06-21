package uz.example.flower.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uz.example.flower.model.JSend;
import uz.example.flower.model.entity.Subject;
import uz.example.flower.repository.SubjectRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubjectService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public JSend saveSubject(List<String> names) {
        List<Subject> subjects = new ArrayList<>();
        names.forEach(name -> {
            Subject subject = new Subject();
            subject.setName(name);
            subjects.add(subject);
        });
        subjectRepository.saveAll(subjects);
        return JSend.success("Subject successful create");
    }
}