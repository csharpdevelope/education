package uz.example.flower.service;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.model.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import uz.example.flower.model.entity.Flower;
import uz.example.flower.model.entity.Images;
import uz.example.flower.repository.ImagesRepository;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
@Slf4j
public class FileDbStorageService {
    private final ImagesRepository imagesRepository;
    private final AuthenticationManager authenticationManager;

    public FileDbStorageService(ImagesRepository imagesRepository, AuthenticationManager authenticationManager) {
        this.imagesRepository = imagesRepository;
        this.authenticationManager = authenticationManager;
    }

    public Images store(MultipartFile file, Flower flower) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Images images = new Images();
        images.setFilename(fileName);
        images.setData(file.getBytes());
        images.setImgType(file.getContentType());
        images.setSize(file.getSize());
        images.setFlower(flower);
        return imagesRepository.save(images);
    }

    public Images uploadFileToDrive(MultipartFile file, Flower flower) throws IOException {
        File fileMeta = new File();
        fileMeta.setName(file.getOriginalFilename());
        java.io.File fileImage = convert(file);
        FileContent fileContent = new FileContent("image/*", fileImage);
        return null;
    }

    private java.io.File convert(MultipartFile file) throws IOException {
        java.io.File convertImage = new java.io.File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convertImage);
        convertImage.createNewFile();
        fos.write(file.getBytes());
        fos.close();
        return convertImage;
    }
}
