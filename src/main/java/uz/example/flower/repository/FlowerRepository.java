package uz.example.flower.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.example.flower.model.entity.Flower;

import java.util.List;

@Repository
public interface FlowerRepository extends JpaRepository<Flower, Long> {

    List<Flower> findAllByIdIn(List<Long> ids);

    Page<Flower> findAllByCategoryIdIn(List<Long> ids, Pageable pageable);
}
