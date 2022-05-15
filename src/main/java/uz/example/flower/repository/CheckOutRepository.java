package uz.example.flower.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.example.flower.model.entity.Checkout;
import uz.example.flower.model.entity.User;

import java.util.List;

@Repository
public interface CheckOutRepository extends JpaRepository<Checkout, Long> {
    List<Checkout> findAllByUser(User user);

    List<Checkout> findAllByFavoriteAndUser(boolean favorite, User user);

    Checkout getByIdAndUser(Long id, User user);
}
