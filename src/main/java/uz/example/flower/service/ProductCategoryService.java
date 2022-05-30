package uz.example.flower.service;

import uz.example.flower.model.JSend;

import java.util.List;

public interface ProductCategoryService {

    JSend getCategory(List<Long> ids, int page, int size);

    JSend getCategory(Long id);

    JSend getCategories();
}