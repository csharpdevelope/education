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

    Optional<Flower> findByIdAndUser(Long id, User user);

    List<Flower> findAllByGiftTypesIn(List<GiftType> giftTypes);

    List<Flower> findAllByGiftTypes(GiftType giftType);

    List<Flower> findAllByUser(User user);

    @Query(value = "select * from flowers", nativeQuery = true)
    List<Flower> findAllFlower();

    @Query("SELECT p FROM Flower p WHERE " +
            "p.name LIKE CONCAT('%',:query, '%')" +
            "Or p.description LIKE CONCAT('%', :query, '%')")
    List<Flower> searchAllByProducts(String query);

    Page<Flower> findAllByUser(User user, Pageable pageable);

    Page<Flower> findAllByGiftTypesIn(List<GiftType> giftTypes, Pageable pageable);
}
