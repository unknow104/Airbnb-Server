package com.techpower.airbnb.request;

import com.google.maps.model.LatLng;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDetails {
    private LatLng latLng;
    private String city;
    private String district;
    private String ward;
}
