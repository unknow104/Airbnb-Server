package com.techpower.airbnb.converter;

import com.techpower.airbnb.dto.CategoryDTO;
import com.techpower.airbnb.entity.CategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter {
    public CategoryDTO toDTO(CategoryEntity entity) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setImage(entity.getImage());
        return dto;
    }

    public CategoryEntity toEntity(CategoryDTO dto) {
        CategoryEntity entity = new CategoryEntity();
        entity.setName(dto.getName());
        entity.setImage(dto.getImage());
        return entity;

    }

    public CategoryEntity toEntity(CategoryDTO dto,CategoryEntity entity) {
        entity.setName(dto.getName());
        if (dto.getImage() != null){
            entity.setImage(dto.getImage());
        }
        return entity;
    }

}
