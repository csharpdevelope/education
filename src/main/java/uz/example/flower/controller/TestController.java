package uz.example.flower.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.example.flower.config.QueueSender;
import uz.example.flower.service.FileManagerService;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("api")
public class TestController {
    private final FileManagerService fileManagerService;

    @Autowired
    private QueueSender queueSender;

    public TestController(FileManagerService fileManagerService) {
        this.fileManagerService = fileManagerService;
    }

    @GetMapping("rabbit")
    public String send() {
        queueSender.send("exchange_test", "router_key_test", "Text Message Flash Energy");
        return "Ok. Done";
    }

    @GetMapping("google/auth0")
    public ResponseEntity<?> getFiles() throws GeneralSecurityException, IOException {
        return ResponseEntity.ok(fileManagerService.listEveryThing());
    }

    @PostMapping("upload/file")
    public ResponseEntity<?> uploadFile(@RequestParam(value = "file")MultipartFile file) throws GeneralSecurityException, IOException {
        return ResponseEntity.ok(fileManagerService.uploadFile(file));
    }
}
