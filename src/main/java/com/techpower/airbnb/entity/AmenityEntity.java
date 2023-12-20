package com.techpower.airbnb.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "amenity")
public class AmenityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "amenity_id")
    private Long id;
    @Column
    private String name;
    @Column
    private String imageUrl;


    @ManyToMany(mappedBy = "amenities")
    private List<RoomEntity> rooms = new ArrayList<>();
}
