package com.techpower.airbnb.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AchievementDTO {
    private Long id;
    private String name;
    private String icon;
}
