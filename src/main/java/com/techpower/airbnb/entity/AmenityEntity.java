package com.techpower.airbnb.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne
    @JoinColumn(name = "room_id")
    private RoomEntity room;
}
