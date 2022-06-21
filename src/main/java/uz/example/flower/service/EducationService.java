package uz.example.flower.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uz.example.flower.model.JSend;
import uz.example.flower.model.entity.Education;
import uz.example.flower.model.entity.Subject;
import uz.example.flower.model.entity.User;
import uz.example.flower.payload.request.EducationDto;
import uz.example.flower.payload.response.EducationResDto;
import uz.example.flower.repository.EducationRepository;
import uz.example.flower.repository.SubjectRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class EducationService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final EducationRepository educationRepository;
    private final SubjectRepository subjectRepository;
    private final ObjectMapper objectMapper;

    public EducationService(EducationRepository educationRepository, SubjectRepository subjectRepository, ObjectMapper objectMapper) {
        this.educationRepository = educationRepository;
        this.subjectRepository = subjectRepository;
        this.objectMapper = objectMapper;
    }

    public JSend postEducation(List<EducationDto> educationDtos, User user) {
        List<Education> educations = new ArrayList<>();
        for (EducationDto educationDto: educationDtos) {
            Education education = new Education();
            education.setHours(educationDto.getHours());
            education.setPurpose(educationDto.getName());
            education.setUser(user);
            Subject subject = subjectRepository.findByName(educationDto.getName());
            education.setSubject(subject);
            educations.add(education);
        }
        educationRepository.saveAll(educations);
        logger.info("Education Successful create");
        return JSend.success("Education Successful create");
    }

    public JsonNode getAllEducation(User currentUser) {
        ObjectNode response = objectMapper.createObjectNode();
        List<Education> educations = educationRepository.findAllByUser(currentUser);
        List<EducationResDto> educationResDtoList = new ArrayList<>();
        int sum = 0;
        for (Education education: educations) {
            EducationResDto educationResDto = new EducationResDto();
            educationResDto.setId(education.getId());
            educationResDto.setPurpose(education.getPurpose());
            educationResDto.setHours(education.getHours());
            educationResDtoList.add(educationResDto);
            sum += education.getHours();
        }
        response.put("sum", sum);
        response.putPOJO("data", educationResDtoList);
        return response;
    }
}
