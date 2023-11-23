package com.techpower.airbnb.entity;

import jakarta.persistence.*;
import lombok.*;

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

}
