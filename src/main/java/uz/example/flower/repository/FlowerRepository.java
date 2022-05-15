package uz.example.flower.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.example.flower.model.entity.Category;
import uz.example.flower.model.entity.Flower;
import uz.example.flower.model.entity.GiftType;
import uz.example.flower.model.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlowerRepository extends JpaRepository<Flower, Long> {

    List<Flower> findAllByIdIn(List<Long> ids);

    Page<Flower> findAllByCategoryIdIn(List<Long> ids, Pageable pageable);

    List<Flower> findAllByUser(User user);

    Optional<Flower> findByIdAndUser(Long id, User user);

    List<Flower> findAllByGiftType(GiftType giftType);

    List<Flower> findAllByUserAndGiftType(User user, GiftType giftType);

    @Query(value = "select * from flowers_table", nativeQuery = true)
    List<Flower> findAllFlower();
}
