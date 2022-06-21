package uz.example.flower.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.example.flower.service.AdminService;

@RestController
@RequestMapping("/api/v1/admin")
@Secured(value = "ADMIN")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("education")
    public ResponseEntity<?> getAll() {
        JsonNode response = adminService.getAllProfessor();
        return ResponseEntity.ok(response);
    }
}
