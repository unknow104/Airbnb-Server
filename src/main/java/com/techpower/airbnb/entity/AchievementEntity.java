package com.techpower.airbnb.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "achievement")
public class AchievementEntity {
    @Id
    @Column(name = "achievement_id")
    private Long id;

    @Column(name = "achievement_name")
    private String name;

    @Column(name = "achievement_icon")
    private String icon;

    @ManyToMany(mappedBy = "achievements")
    private Set<UserEntity> user= new HashSet<>();
}
