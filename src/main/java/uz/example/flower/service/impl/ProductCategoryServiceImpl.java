package uz.example.flower.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.example.flower.model.JSend;
import uz.example.flower.model.dto.FlowerResponse;
import uz.example.flower.model.entity.Flower;
import uz.example.flower.payload.response.CategoryDto;
import uz.example.flower.repository.FlowerRepository;
import uz.example.flower.service.ProductCategoryService;
import uz.example.flower.service.tools.FlowerMapping;

import java.util.List;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final FlowerRepository flowerRepository;

    public ProductCategoryServiceImpl(FlowerRepository flowerRepository) {
        this.flowerRepository = flowerRepository;
    }

    @Override
    public JSend getCategory(List<Long> ids, int page, int size) {
        CategoryDto response = new CategoryDto();
        Pageable pageable = PageRequest.of(page, size);
        Page<Flower> flowers;
        if (ids.isEmpty())
        {
            flowers = flowerRepository.findAll(pageable);
        } else {
            flowers = flowerRepository.findAllByCategoryIdIn(ids, pageable);
        }
        response.setPage(flowers.getPageable().getPageNumber());
        response.setPageSize(flowers.getSize());
        response.setTotalSize(flowers.getTotalPages());
        response.setTotalPage(flowers.getTotalElements());
        List<Flower> listFlower = flowers.getContent();
        List<FlowerResponse> responseFlower = FlowerMapping.toFlowerResponse(listFlower);
        response.setProduct(responseFlower);
        return JSend.success(response);
    }
}
