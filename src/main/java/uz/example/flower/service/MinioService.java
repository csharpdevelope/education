package uz.example.flower.service;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.example.flower.model.JSend;
import uz.example.flower.model.entity.Flower;
import uz.example.flower.model.entity.Images;
import uz.example.flower.repository.ImagesRepository;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class MinioService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final MinioClient minioClient;
    private final ImagesRepository imagesRepository;
    @Value("${minio.bucketName}")
    private String bucketName;
    @Value("${minio.url}")
    private String url;

    public MinioService(MinioClient minioClient, ImagesRepository imagesRepository) {
        this.minioClient = minioClient;
        this.imagesRepository = imagesRepository;
    }

    public JSend uploadFile(MultipartFile multipartFile, Flower flower) {
        try {
            InputStream inputStream = new ByteArrayInputStream(multipartFile.getBytes());
            String originalName = multipartFile.getOriginalFilename();
            String objectName = generateMinioObjectName(originalName);
            String contentType = multipartFile.getContentType();
            Map<String, String> headers = new HashMap<>();
            headers.put("X-Amz-Storage-Class", "REDUCED_REDUNDANCY");
            Map<String, String> userMetadata = new HashMap<>();
            userMetadata.put("Project", "FlowerShopping");

            minioClient.putObject(
                    PutObjectArgs
                            .builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, multipartFile.getSize(), -1)
                            .headers(headers)
                            .contentType(contentType)
                            .build()
            );
            Images images = new Images();
            images.setFilename(objectName);
            images.setImgType(contentType);
            images.setSize(multipartFile.getSize());
            String url = "/flower-shopping/" + objectName;
            images.setImgUrl(url);
            images.setActive(true);
            images.setFlower(flower);
            imagesRepository.save(images);
            return JSend.success(images);
        } catch (MinioException e) {
            logger.error("Minio Exception: ", e);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            logger.error("Exceptions error: ", e);
        }
        return JSend.fail(500, "Internal Server Error");
    }

    public JSend getAllBucket() {
        try {
            return JSend.success(minioClient.listBuckets());
        } catch (ServerException |
                InsufficientDataException |
                ErrorResponseException |
                IOException |
                NoSuchAlgorithmException |
                InvalidKeyException |
                InvalidResponseException |
                XmlParserException |
                InternalException e) {
            e.printStackTrace();
            logger.error("MinioException error: ", e);
            return JSend.fail(e.getMessage());
        }
    }

    public JSend getFileByObjectName(Images images) {
        try {
            GetObjectResponse response = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(images.getFilename())
                    .build());

            InputStream inputStream = new ByteArrayInputStream(response.readAllBytes());
            return JSend.success(inputStream);
        } catch (ServerException |
                InsufficientDataException |
                ErrorResponseException |
                IOException |
                NoSuchAlgorithmException |
                InvalidKeyException |
                InvalidResponseException |
                XmlParserException |
                InternalException e) {
            e.printStackTrace();
            logger.error("MinioException error: ", e);
            return JSend.fail(e.getMessage());
        }
    }

    private String generateMinioObjectName(String name) {
        String randomString = UUID.randomUUID().toString();
        int index = name.indexOf('.');
        String originalName = name.substring(0, index);
        String extension = name.substring(index);
        return originalName.concat(randomString).concat(extension);
    }
}
