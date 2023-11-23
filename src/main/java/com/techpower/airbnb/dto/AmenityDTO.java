package com.techpower.airbnb.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AmenityDTO {
    private Long id;
    private String name;
    private String imageUrl;

}