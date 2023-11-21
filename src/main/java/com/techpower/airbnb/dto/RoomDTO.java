package com.techpower.airbnb.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.util.Date;
import java.util.List;

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
    private boolean washingMachine;
    private boolean television;
    private boolean airConditioner;
    private boolean wifi;
    private boolean kitchen;
    private boolean parking;
    private boolean pool;
    private boolean hotAndColdMachine;
    private Date created_at;
    private Date updated_at;
    private int maxGuests;
    private int numLivingRooms;
    private int numBathrooms;
    private int numBedrooms;
}
