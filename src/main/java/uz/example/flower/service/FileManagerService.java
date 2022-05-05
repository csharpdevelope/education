package uz.example.flower.service;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public class FileManagerService {
    private final GoogleDriveService googleDriveService;

    public FileManagerService(GoogleDriveService googleDriveService) {
        this.googleDriveService = googleDriveService;
    }

    public List<File> listEveryThing() throws GeneralSecurityException, IOException {
        FileList result = googleDriveService.getInstance()
                .files()
                .list()
                .setPageSize(10)
                .setFields("nextPageToken, files(id, name)")
                .execute();

        return result.getFiles();
    }

    public File uploadFile(MultipartFile multipartFile) throws GeneralSecurityException, IOException {
        File fileMetaData = new File();
        fileMetaData.setName(multipartFile.getOriginalFilename());
        java.io.File filePath = new java.io.File("files/" + multipartFile.getOriginalFilename());
        FileContent fileContent = new FileContent(multipartFile.getContentType(), filePath);
        File file = googleDriveService.getInstance()
                .files()
                .create(fileMetaData, fileContent)
                .setFields("id")
                .execute();
        System.out.println("File Id: " + file.getId());
        filePath.delete();
        return file;
    }
}
