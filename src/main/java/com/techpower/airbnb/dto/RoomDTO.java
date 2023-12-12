package com.techpower.airbnb.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
    private Long id;
    private String name;
    private String description;
    private double totalStar;
    private double price;
    private List<String> images;
    private String codeLocation;
    private AddressDTO address;
    private boolean available;
    private boolean allowPet;
    private List<AmenityDTO> amenities;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private int maxGuests;
    private int numLivingRooms;
    private int numBathrooms;
    private int numBedrooms;
}
