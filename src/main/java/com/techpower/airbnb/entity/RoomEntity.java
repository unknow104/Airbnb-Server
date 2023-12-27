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
    private boolean allowPet;
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

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "room_category",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<CategoryEntity> category = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "room_amenity",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    private List<AmenityEntity> amenities = new ArrayList<>();
}
