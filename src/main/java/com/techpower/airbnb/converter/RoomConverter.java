package com.techpower.airbnb.converter;

import com.techpower.airbnb.dto.AmenityDTO;
import com.techpower.airbnb.dto.RoomDTO;
import com.techpower.airbnb.entity.AmenityEntity;
import com.techpower.airbnb.entity.ImageRoomEntity;
import com.techpower.airbnb.entity.RoomEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RoomConverter {
    @Autowired
    private AddressConverter addressConverter;
    @Autowired
    private AmenityConverter amenityConverter;

    public RoomEntity toEntity(RoomDTO roomDTO) {
        List<AmenityEntity> amenities = roomDTO.getAmenities()
                .stream()
                .map(amenityConverter::toEntity)
                .toList();
        return RoomEntity.builder()
                .name(roomDTO.getName())
                .description(roomDTO.getDescription())
                .price(roomDTO.getPrice())
                .address(addressConverter.toEntity(roomDTO.getAddress()))
                .amenities(amenities)
                .allowPet(roomDTO.isAllowPet())
                .maxGuests(roomDTO.getMaxGuests())
                .numLivingRooms(roomDTO.getNumLivingRooms())
                .numBathrooms(roomDTO.getNumBathrooms())
                .numBedrooms(roomDTO.getNumBedrooms())
                .build();

    }

    public RoomDTO toDTO(RoomEntity roomEntity) {
        List<String> images = new ArrayList<>();
        if (roomEntity.getImages() != null) {
            for (ImageRoomEntity image : roomEntity.getImages()) {
                images.add(image.getUrlImage());
            }
        }
        List<AmenityDTO> amenityDTOs = roomEntity.getAmenities()
                .stream()
                .map(amenityConverter::toDTO)
                .collect(Collectors.toList());

        return RoomDTO.builder()
                .id(roomEntity.getId())
                .name(roomEntity.getName())
                .description(roomEntity.getDescription())
                .price(roomEntity.getPrice())
                .images(images)
                .created_at(roomEntity.getCreated_at())
                .updated_at(roomEntity.getUpdated_at())
                .address(addressConverter.apply(roomEntity.getAddress()))
                .codeLocation(roomEntity.getLocation().getName())
                .amenities(amenityDTOs)
                .allowPet(roomEntity.isAllowPet())
                .maxGuests(roomEntity.getMaxGuests())
                .numLivingRooms(roomEntity.getNumLivingRooms())
                .numBedrooms(roomEntity.getNumBedrooms())
                .numBathrooms(roomEntity.getNumBathrooms())
                .available(true)
                .build();

    }

    public List<RoomDTO> toDTOs(List<RoomEntity> roomEntities) {
        List<RoomDTO> roomDTOS = new ArrayList<>();
        for (RoomEntity roomEntity : roomEntities) {
            roomDTOS.add(toDTO(roomEntity));
        }
        return roomDTOS;
    }

    public RoomEntity toEntity(RoomDTO dto, RoomEntity entity) {
        List<AmenityEntity> amenities = dto.getAmenities()
                .stream()
                .map(amenityConverter::toEntity)
                .collect(Collectors.toList());
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setAmenities(amenities);
        entity.setCreated_at(dto.getCreated_at());
        entity.setUpdated_at(dto.getUpdated_at());
        return entity;
    }
}
