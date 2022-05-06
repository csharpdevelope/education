package uz.example.flower.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.example.flower.model.entity.Flower;
import uz.example.flower.model.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlowerRepository extends JpaRepository<Flower, Long> {

    List<Flower> findAllByIdIn(List<Long> ids);

    Page<Flower> findAllByCategoryIdIn(List<Long> ids, Pageable pageable);

    List<Flower> findAllByUser(User user);

    Optional<Flower> findByIdAndUser(Long id, User user);
}
