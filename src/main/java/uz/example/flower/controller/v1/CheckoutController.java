package uz.example.flower.controller.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.example.flower.model.JSend;
import uz.example.flower.payload.request.CheckOutDto;
import uz.example.flower.service.CheckOutService;
import uz.example.flower.component.SecurityUtils;

@RestController
@RequestMapping("api/v1/checkout")
public class CheckoutController {
    private final SecurityUtils securityUtils;
    private final CheckOutService checkOutService;

    public CheckoutController(SecurityUtils securityUtils, CheckOutService checkOutService) {
        this.securityUtils = securityUtils;
        this.checkOutService = checkOutService;
    }

    @PostMapping("create")
    public ResponseEntity<?> create(@RequestBody CheckOutDto checkOutDto) {
        JSend response = checkOutService.create(checkOutDto, securityUtils.getCurrentUser());
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @PatchMapping("change/{id}")
    public ResponseEntity<?> change(@PathVariable("id") Long checkOutId, @RequestBody CheckOutDto checkOutDto) {
        JSend response = checkOutService.change(checkOutId, checkOutDto, securityUtils.getCurrentUser());
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @GetMapping("get")
    public ResponseEntity<?> getAllCheckOut() {
        JSend response = checkOutService.getAllCheckOut(securityUtils.getCurrentUser());
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long checkOutId) {
        JSend response = checkOutService.delete(checkOutId, securityUtils.getCurrentUser());
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }
}
