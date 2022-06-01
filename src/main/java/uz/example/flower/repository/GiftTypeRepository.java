package uz.example.flower.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.example.flower.model.entity.GiftType;
import uz.example.flower.model.enums.GiftTypeEnum;

import java.util.List;
import java.util.Optional;

@Repository
public interface GiftTypeRepository extends JpaRepository<GiftType, Long> {

    Optional<GiftType> findByName(String name);

    List<GiftType> findAllByNameIn(List<String> names);
}
