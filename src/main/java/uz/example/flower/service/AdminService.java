package uz.example.flower.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.example.flower.model.entity.Education;
import uz.example.flower.repository.EducationRepository;
import uz.example.flower.repository.SubjectRepository;

import java.util.List;

@Service
@Transactional
public class AdminService {
    private final EducationRepository educationRepository;
    private final SubjectRepository subjectRepository;

    public AdminService(EducationRepository educationRepository, SubjectRepository subjectRepository) {
        this.educationRepository = educationRepository;
        this.subjectRepository = subjectRepository;
    }

    @Transactional(readOnly = true)
    public JsonNode getAllProfessor() {
        List<Education> educations = educationRepository.findAll();
        return null;
    }
}
