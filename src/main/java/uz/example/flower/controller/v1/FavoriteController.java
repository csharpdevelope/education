package uz.example.flower.controller.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.example.flower.model.JSend;
import uz.example.flower.service.FavoriteService;
import uz.example.flower.component.SecurityUtils;

@RestController
@RequestMapping("api/v1/favorite")
public class FavoriteController {
    private final FavoriteService favoriteService;
    private final SecurityUtils securityUtils;

    public FavoriteController(FavoriteService favoriteService, SecurityUtils securityUtils) {
        this.favoriteService = favoriteService;
        this.securityUtils = securityUtils;
    }

    @PostMapping("create")
    public ResponseEntity<?> create(@RequestBody Long flower_id) {
        JSend response = favoriteService.create(flower_id, securityUtils.getCurrentUser());
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long favoriteId) {
        JSend response = favoriteService.delete(favoriteId, securityUtils.getCurrentUser());
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @GetMapping("get")
    public ResponseEntity<?> getFavorites() {
        JSend response = favoriteService.getFavorites(securityUtils.getCurrentUser());
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }
}
