package com.techpower.airbnb.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @Column
    private LocalDateTime created_at;
    @Column
    private LocalDateTime updated_at;
    @Column
    private boolean hotAndColdMachine;
    @Column
    private boolean washingMachine;
    @Column
    private boolean television;
    @Column
    private boolean airConditioner;
    @Column
    private boolean wifi;
    @Column
    private boolean kitchen;
    @Column
    private boolean parking;
    @Column
    private boolean pool;
    @Column
    private boolean allowPet;
    @Column(name = "num_living_rooms")
    private int numLivingRooms;
    @Column(name = "num_bathrooms")
    private int numBathrooms;
    @Column(name = "num_bedrooms")
    private int numBedrooms;
    @Column(name = "maxGuest")
    private int maxGuest;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "address_id")
    private AddressEntity address;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @OneToMany(mappedBy = "room")
    private List<ImageRoomEntity> images = new ArrayList<>();
    @OneToMany(mappedBy = "room")
    private List<OrderEntity> orders = new ArrayList<>();
    @OneToMany(mappedBy = "room")
    private List<WishlistEntity> wishlists;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private LocationEntity location;
}
