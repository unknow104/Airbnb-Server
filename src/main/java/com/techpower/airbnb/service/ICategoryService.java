package com.techpower.airbnb.service;

import com.techpower.airbnb.dto.CategoryDTO;

import java.util.List;

public interface ICategoryService {
    List<CategoryDTO> getAll();

    List<CategoryDTO> getAllByRoom(long roomId);

    CategoryDTO save(CategoryDTO categoryDTO);

    CategoryDTO update(CategoryDTO categoryDTO);

    void removeByRoom(long roomId);

    String removeByCategory(long categoryId);
}
