package uz.example.flower.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.example.flower.model.entity.Images;

@Repository
public interface ImagesRepository extends JpaRepository<Images, Long> {
    Images findByFilename(String filename);
}
