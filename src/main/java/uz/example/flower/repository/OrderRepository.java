package uz.example.flower.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.example.flower.model.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
