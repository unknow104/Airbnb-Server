package com.techpower.airbnb.converter;

import com.techpower.airbnb.dto.AmenityDTO;
import com.techpower.airbnb.entity.AmenityEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AmenityConverter {

    public AmenityEntity toEntity(AmenityDTO amenityDTO) {
        return AmenityEntity.builder()
                .name(amenityDTO.getName())
                .imageUrl(amenityDTO.getImageUrl())
                .build();
    }

    public AmenityDTO toDTO(AmenityEntity amenityEntity){
        return AmenityDTO.builder()
                .id(amenityEntity.getId())
                .name(amenityEntity.getName())
                .imageUrl(amenityEntity.getImageUrl())
                .build();
    }


    public List<AmenityDTO> toDTOs(List<AmenityEntity> amenityEntities) {
        List<AmenityDTO> amenityDTOs = new ArrayList<>();
        for(AmenityEntity amenityEntity:amenityEntities) {
            amenityDTOs.add(toDTO(amenityEntity));
        }
        return amenityDTOs;
    }

    public AmenityEntity toEntity(AmenityDTO amenityDTO, AmenityEntity amenityEntity) {
        amenityEntity.setId(amenityDTO.getId());
        amenityEntity.setName(amenityDTO.getName());
        amenityEntity.setImageUrl(amenityDTO.getImageUrl());
        return amenityEntity;
    }
}
