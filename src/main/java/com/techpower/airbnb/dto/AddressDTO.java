package com.techpower.airbnb.dto;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
    private String fullAddress;
    private String city;
    private String district;
    private String ward;
    private double lat;
    private double lng;
}
