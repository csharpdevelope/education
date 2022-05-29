package uz.example.flower.service;

import org.springframework.stereotype.Service;
import uz.example.flower.model.JSend;
import uz.example.flower.model.entity.Checkout;
import uz.example.flower.model.entity.Flower;
import uz.example.flower.model.entity.User;
import uz.example.flower.payload.response.CheckOutResponse;
import uz.example.flower.repository.CheckOutRepository;

import java.util.List;

@Service
public class FavoriteService {
    private final CheckOutRepository checkOutRepository;
    private final FlowerService flowerService;
    private final CheckOutService checkOutService;

    public FavoriteService(CheckOutRepository checkOutRepository, FlowerService flowerService, CheckOutService checkOutService) {
        this.checkOutRepository = checkOutRepository;
        this.flowerService = flowerService;
        this.checkOutService = checkOutService;
    }

    public JSend create(Long flowerId, User user) {
        Checkout checkout = new Checkout();
        Flower flower = flowerService.getById(flowerId);
        checkout.setActive(true);
        checkout.setUser(user);
        checkout.setFlower(flower);
        checkOutRepository.save(checkout);
        return JSend.success();
    }

    public JSend getFavorites(User user) {
        List<Checkout> checkouts = checkOutRepository.findAllByFavoriteAndUser(true, user);
        List<CheckOutResponse> responses = checkOutService.getUserFavoriteOrCheckOut(checkouts);
        return JSend.success(responses);
    }

    public JSend delete(Long favoriteId, User user) {
        Checkout checkout = checkOutRepository.getByIdAndUser(favoriteId, user);
        checkOutRepository.delete(checkout);
        return JSend.success();
    }
}
