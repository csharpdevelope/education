package uz.example.flower.service;

import org.springframework.stereotype.Service;
import uz.example.flower.model.JSend;
import uz.example.flower.model.entity.Category;
import uz.example.flower.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoriesService {
    private final CategoryRepository categoryRepository;

    public CategoriesService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public JSend getCategory() {
        List<Category> categories = categoryRepository.findAll();
        return JSend.success(categories);
    }
}
