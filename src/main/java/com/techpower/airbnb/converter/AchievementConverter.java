package com.techpower.airbnb.converter;

import com.techpower.airbnb.dto.AchievementDTO;
import com.techpower.airbnb.entity.AchievementEntity;
import org.springframework.stereotype.Component;

@Component
public class AchievementConverter {


    public AchievementDTO toDTO(AchievementEntity entity) {
        AchievementDTO dto = new AchievementDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setIcon(entity.getIcon());
        return dto;
    }

    public AchievementEntity toEntity(AchievementDTO dto) {
        AchievementEntity entity = new AchievementEntity();
        entity.setIcon(entity.getIcon());
        entity.setName(entity.getName());
        entity.setUser(entity.getUser());
        return entity;
    }
}
