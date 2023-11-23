package com.techpower.airbnb.api;

import com.google.maps.errors.ApiException;
import com.techpower.airbnb.dto.AmenityDTO;
import com.techpower.airbnb.service.IAmenityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/amenity")
public class AmenityAPI {
    @Autowired(required = true)
    IAmenityService iAmenityService;

    @GetMapping
    public ResponseEntity<List<AmenityDTO>> findAll() {
        List<AmenityDTO> amenityDTOs = iAmenityService.findAll();
        return !amenityDTOs.isEmpty() ? ResponseEntity.ok(amenityDTOs) : ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<?> save(@PathVariable AmenityDTO dto) throws IOException, InterruptedException, ApiException {
        AmenityDTO amenityDTO = AmenityDTO.builder()
                .id(dto.getId())
                .name(dto.getName())
                .imageUrl(dto.getImageUrl())
                .build();
        AmenityDTO amenityDTOSave = iAmenityService.save(amenityDTO);
        if (amenityDTOSave == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi khi tạo dịch vụ.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(amenityDTOSave);
    }

    @PutMapping("/{idAmenity}")
    public ResponseEntity<AmenityDTO> update(@PathVariable("idAmenity") long idRoom,
                                             @PathVariable AmenityDTO dto) throws IOException, InterruptedException, ApiException {
        AmenityDTO amenityDTO = AmenityDTO.builder()
                .id(dto.getId())
                .name(dto.getName())
                .imageUrl(dto.getImageUrl())
                .build();
        AmenityDTO amenityDTOSave = iAmenityService.update(amenityDTO);

        return ResponseEntity.status(HttpStatus.OK).body(amenityDTOSave);
    }

    @DeleteMapping("/{idAmenity}")
    public ResponseEntity<?> delete(@PathVariable("idAmenity") Long idAmenity) {
        return ResponseEntity.ok(iAmenityService.delete(idAmenity));
    }
}
