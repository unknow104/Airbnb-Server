package com.techpower.airbnb.service;

import com.google.maps.errors.ApiException;
import com.techpower.airbnb.dto.AmenityDTO;

import com.techpower.airbnb.request.SearchHouseRequest;
import java.io.IOException;
import java.util.List;

public interface IAmenityService {
    List<AmenityDTO> findAll();
    AmenityDTO findOneById(long id);
    AmenityDTO save(AmenityDTO dto) throws IOException, InterruptedException, ApiException;
    AmenityDTO update(AmenityDTO dto) throws IOException, InterruptedException, ApiException;
    List<AmenityDTO> search(SearchHouseRequest request);
    String delete(Long idAmenity);
}
