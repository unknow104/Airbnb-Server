package com.techpower.airbnb.converter;

import com.techpower.airbnb.dto.AddressDTO;
import com.techpower.airbnb.entity.AddressEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
@Component
public class AddressConverter implements Function<AddressEntity, AddressDTO> {
    @Override
    public AddressDTO apply(AddressEntity addressEntity) {
        return AddressDTO.builder()
                .fullAddress(addressEntity.getFullAddress())
                .city(addressEntity.getCity())
                .ward(addressEntity.getWard())
                .district(addressEntity.getDistrict())
                .lat(addressEntity.getLat())
                .lng(addressEntity.getLng())
                .build();
    }

    public AddressEntity toEntity(AddressDTO addressDTO) {
        return AddressEntity.builder()
                .fullAddress(addressDTO.getFullAddress())
                .ward(addressDTO.getWard())
                .city(addressDTO.getCity())
                .district(addressDTO.getDistrict())
                .lat(addressDTO.getLat())
                .lng(addressDTO.getLng())
                .build();
    }

    public List<AddressDTO> mapperList(List<AddressEntity> addressEntities) {
        return addressEntities.stream().map(this::apply).toList();
    }
}
