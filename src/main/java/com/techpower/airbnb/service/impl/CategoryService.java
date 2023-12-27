package com.techpower.airbnb.service.impl;

import com.techpower.airbnb.converter.CategoryConverter;
import com.techpower.airbnb.dto.CategoryDTO;
import com.techpower.airbnb.entity.CategoryEntity;
import com.techpower.airbnb.repository.CategoryRepository;
import com.techpower.airbnb.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryConverter categoryConverter;


    @Override
    public List<CategoryDTO> getAll() {
        List<CategoryDTO> result = new ArrayList<>();
        for (CategoryEntity entity : categoryRepository.findAll()) {
            CategoryDTO dto = categoryConverter.toDTO(entity);
            result.add(dto);
        }
        return result;
    }

    @Override
    public List<CategoryDTO> getAllByRoom(long roomId) {
        return null;
    }

    @Override
    public CategoryDTO save(CategoryDTO categoryDTO) {
        CategoryEntity categoryEntity = categoryConverter.toEntity(categoryDTO);
        CategoryEntity saveResult = categoryRepository.save(categoryEntity);
        return categoryConverter.toDTO(saveResult);
    }

    @Override
    public CategoryDTO update(CategoryDTO categoryDTO) {
        CategoryEntity categoryEntity = categoryRepository.getById(categoryDTO.getId());
        categoryEntity.setName(categoryDTO.getName());
        categoryEntity.setImage(categoryDTO.getImage());
        return categoryConverter.toDTO(categoryRepository.save(categoryEntity));
    }

    @Override
    public void removeByRoom(long roomId) {
        return;
    }


    @Override
    public String removeByCategory(long categoryId) {
        if (categoryRepository.existsById(categoryId)) {
            categoryRepository.deleteById(categoryId);
            return "Category has been deleted";
        } else {
            return "Category not found";
        }
    }
}
