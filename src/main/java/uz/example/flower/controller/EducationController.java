package uz.example.flower.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.example.flower.component.SecurityUtils;
import uz.example.flower.model.JSend;
import uz.example.flower.payload.request.EducationDto;
import uz.example.flower.service.EducationService;

import java.util.List;

@RestController
@RequestMapping("api/v1/education")
public class EducationController {

    private final EducationService educationService;
    private final SecurityUtils securityUtils;

    public EducationController(EducationService educationService, SecurityUtils securityUtils) {
        this.educationService = educationService;
        this.securityUtils = securityUtils;
    }

    @PostMapping("save")
    public ResponseEntity<?> postEducation(@RequestBody List<EducationDto> educationDtos) {
        JSend response = educationService.postEducation(educationDtos, securityUtils.getCurrentUser());
        return ResponseEntity.ok(response);
    }

    @GetMapping("all")
    public ResponseEntity<?> getAllEducation() {
        JsonNode response = educationService.getAllEducation(securityUtils.getCurrentUser());
        return ResponseEntity.ok(response);
    }
}