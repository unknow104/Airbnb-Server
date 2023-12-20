package com.techpower.airbnb.service.impl;

import com.google.maps.errors.ApiException;
import com.techpower.airbnb.converter.AmenityConverter;
import com.techpower.airbnb.dto.AmenityDTO;
import com.techpower.airbnb.entity.AmenityEntity;
import com.techpower.airbnb.repository.AmenityRepository;
import com.techpower.airbnb.request.SearchHouseRequest;
import com.techpower.airbnb.service.IAmenityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class AmenityService implements IAmenityService {

    @Autowired
    AmenityConverter amenityConverter;
    @Autowired
    AmenityRepository amenityRepository;

    @Override
    public List<AmenityDTO> findAll() {
        return amenityConverter.toDTOs(amenityRepository.findAll());
    }

    @Override
    public AmenityDTO findOneById(long id) {
        return amenityConverter.toDTO(amenityRepository.findById(id).get());
    }

    @Override
    public AmenityDTO save(AmenityDTO dto) throws IOException, InterruptedException, ApiException {
        AmenityEntity amenityEntity = amenityConverter.toEntity(dto);
        AmenityEntity saveAmenity = amenityRepository.save(amenityEntity);
        return amenityConverter.toDTO(saveAmenity);
    }

    @Override
    public AmenityDTO update(AmenityDTO dto) throws IOException, InterruptedException, ApiException {
        AmenityEntity existAmenity = amenityRepository.getById(dto.getId());
        existAmenity.setName(dto.getName());
        existAmenity.setImageUrl(dto.getImageUrl());
        return amenityConverter.toDTO(amenityRepository.save(existAmenity));
    }

    @Override
    public List<AmenityDTO> search(SearchHouseRequest request) {
        return null;
    }

    @Override
    public String delete(Long idAmenity) {
        if(amenityRepository.existsById(idAmenity)) {
            amenityRepository.deleteById(idAmenity);
            return "Amenity has been deleted successfully.";
        }else{
            return "Amenity does not exist.";
        }
    }
}
