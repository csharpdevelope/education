package uz.example.flower.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uz.example.flower.model.JSend;
import uz.example.flower.model.entity.Checkout;
import uz.example.flower.model.entity.Flower;
import uz.example.flower.model.entity.User;
import uz.example.flower.payload.request.CheckOutDto;
import uz.example.flower.payload.response.CheckOutResponse;
import uz.example.flower.repository.CheckOutRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CheckOutService {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private final CheckOutRepository checkOutRepository;
    private final FlowerService flowerService;

    public CheckOutService(CheckOutRepository checkOutRepository, FlowerService flowerService) {
        this.checkOutRepository = checkOutRepository;
        this.flowerService = flowerService;
    }

    public JSend create(CheckOutDto checkOutDto, User user) {
        LOG.info("CheckOut create");
        Checkout checkout = new Checkout();
        Flower flower = flowerService.getById(checkOutDto.getFlowerId());
        checkout.setFlower(flower);
        checkout.setCount(checkOutDto.getCount());
        checkout.setUser(user);
        checkOutRepository.save(checkout);
        return JSend.success();
    }

    public JSend getAllCheckOut(User user) {
        List<Checkout> checkouts = checkOutRepository.findAllByUser(user);
        List<CheckOutResponse> responses = getUserFavoriteOrCheckOut(checkouts);
        return JSend.success(responses);
    }

    public JSend delete(Long id, User user) {
        Checkout checkout = checkOutRepository.getByIdAndUser(id, user);
        checkOutRepository.delete(checkout);
        return JSend.success();
    }

    public JSend change(Long checkOutId, CheckOutDto checkOutDto, User currentUser) {
        Checkout checkout = checkOutRepository.getByIdAndUser(checkOutId, currentUser);
        checkout.setCount(checkOutDto.getCount());
        checkOutRepository.save(checkout);
        return JSend.success("success");
    }

    public List<CheckOutResponse> getUserFavoriteOrCheckOut(List<Checkout> checkouts) {
        List<CheckOutResponse> responses = new ArrayList<>();
        for (Checkout checkout : checkouts) {
            Flower flower = checkout.getFlower();
            CheckOutResponse checkOutResponse = new CheckOutResponse(
                    checkout.getId(),
                    flower.getName(),
                    checkout.getCount(),
                    "",
                    flower.getPrice(),
                    flower.getDiscount(),
                    checkout.isFavorite()
            );

            responses.add(checkOutResponse);
        }
        return responses;
    }
}
