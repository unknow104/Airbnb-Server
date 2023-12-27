package com.techpower.airbnb.api;

import com.techpower.airbnb.dto.CategoryDTO;
import com.techpower.airbnb.service.ICategoryService;
import com.techpower.airbnb.service.impl.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryApi {

    private final ICategoryService iCategoryService;
    private final CloudinaryService cloudinaryService;

    @GetMapping("")
    public ResponseEntity<List<CategoryDTO>> getAll() {
        List<CategoryDTO> result = iCategoryService.getAll();
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            return ResponseEntity.ok(result);
        }
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> save(
            @PathVariable("name") String name,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        CategoryDTO categoryDTO = CategoryDTO.builder()
                .name(name)
                .image(cloudinaryService.uploadImage(image))
                .build();
        CategoryDTO result = iCategoryService.save(categoryDTO);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.ok(result);
        }
    }

    @PutMapping("/{idCategory}")
    public ResponseEntity<CategoryDTO> update(
            @PathVariable("idCategory") long idCategory,
            @PathVariable("name") String name,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        CategoryDTO categoryDTO = CategoryDTO.builder()
                .id(idCategory)
                .name(name)
                .image(cloudinaryService.uploadImage(image))
                .build();
        CategoryDTO result = iCategoryService.update(categoryDTO);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.ok(result);
        }
    }

    @DeleteMapping("/{idCategory}")
    public ResponseEntity<String> remove(
            @PathVariable("idCategory") Long idCategory
    ) {
        String result = iCategoryService.removeByCategory(idCategory);
        if (result.equals("Category not found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        } else {
            return ResponseEntity.ok(result);
        }
    }
}
