package com.techpower.airbnb.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "room")
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private double price;
    @OneToMany(mappedBy = "room")
    private List<AmenityEntity> amenities;
    @Column
    private LocalDateTime created_at;
    @Column
    private LocalDateTime updated_at;
    @Column
    private boolean hotAndColdMachine;
    @Column(name = "num_living_rooms")
    private int numLivingRooms;
    @Column(name = "num_bathrooms")
    private int numBathrooms;
    @Column(name = "num_bedrooms")
    private int numBedrooms;
    @Column(name = "max_guests")
    private int maxGuests;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "address_id")
    private AddressEntity address;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private LocationEntity location;
    @OneToMany(mappedBy = "room")
    private List<ImageRoomEntity> images = new ArrayList<>();

    @OneToMany(mappedBy = "room")
    private List<OrderEntity> orders = new ArrayList<>();

    @OneToMany(mappedBy = "room")
    private List<WishlistEntity> wishlists;

}
