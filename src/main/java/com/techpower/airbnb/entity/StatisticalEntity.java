package com.techpower.airbnb.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.ZoneId;

@Getter
@Setter
@Entity
@Table(name = "statistical")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statistical_id")
    private Long id;
    @Column
    private int year;
    @Column
    private int month;
    @Column
    private int day;
    @Column
    private double totalRevenue;
    @Column
    private double reallyReceived;
    @ManyToOne
    @JoinColumn(name = "used_id")
    private UserEntity user;

    public StatisticalEntity(UserEntity user) {
        this.user = user;
        this.year = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")).getYear();
        this.month = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")).getMonthValue();
        this.day = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")).getDayOfMonth();
    }
}
