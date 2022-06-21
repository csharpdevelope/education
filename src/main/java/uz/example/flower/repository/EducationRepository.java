package uz.example.flower.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.example.flower.model.entity.Education;
import uz.example.flower.model.entity.User;

import java.util.List;

@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {
    List<Education> findAllByUser(User user);
}
