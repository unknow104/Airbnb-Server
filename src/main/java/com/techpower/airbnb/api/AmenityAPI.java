package com.techpower.airbnb.api;

import com.google.maps.errors.ApiException;
import com.techpower.airbnb.dto.AmenityDTO;
import com.techpower.airbnb.service.IAmenityService;
import com.techpower.airbnb.service.impl.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/amenity")
public class AmenityAPI {
    @Autowired(required = true)
    IAmenityService iAmenityService;
    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping
    public ResponseEntity<List<AmenityDTO>> findAll() {
        List<AmenityDTO> amenityDTOs = iAmenityService.findAll();
        return !amenityDTOs.isEmpty() ? ResponseEntity.ok(amenityDTOs) : ResponseEntity.noContent().build();
    }

    @GetMapping("/{amenityId}")
    public ResponseEntity<AmenityDTO> getOne(@PathVariable("amenityId") Long id) {
        if (iAmenityService.findOneById(id) != null) {
            return ResponseEntity.ok(iAmenityService.findOneById(id));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("")
    public ResponseEntity<?> save(
            @RequestParam("name") String name,
            @RequestParam(value = "imageUrl",required = false) MultipartFile imageUrl
    ) throws IOException, InterruptedException, ApiException {
        try {
            AmenityDTO amenityDTO = AmenityDTO.builder()
                    .name(name)
                    .imageUrl(cloudinaryService.uploadImage(imageUrl))
                    .build();
            AmenityDTO amenityDTOSave = iAmenityService.save(amenityDTO);

            if (amenityDTOSave == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi khi tạo dịch vụ.");
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(amenityDTOSave);
        } catch (Exception e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
    @PutMapping("/{idAmenity}")
    public ResponseEntity<AmenityDTO> update(@PathVariable("idAmenity") long idRoom
            , @RequestParam("name") String name
            , @RequestParam("imageUrl") String imageUrl) throws IOException, InterruptedException, ApiException {
        AmenityDTO amenityDTO = AmenityDTO.builder()
                .id(idRoom)
                .name(name)
                .imageUrl(imageUrl)
                .build();
        AmenityDTO amenityDTOSave = iAmenityService.update(amenityDTO);

        return ResponseEntity.status(HttpStatus.OK).body(amenityDTOSave);
    }

    @DeleteMapping("/{idAmenity}")
    public ResponseEntity<?> delete(@PathVariable("idAmenity") Long idAmenity) {
        return ResponseEntity.ok(iAmenityService.delete(idAmenity));
    }
}
