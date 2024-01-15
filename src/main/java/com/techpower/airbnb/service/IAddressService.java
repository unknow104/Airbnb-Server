package com.techpower.airbnb.service;

import com.techpower.airbnb.dto.AddressDTO;

import java.util.List;

public interface IAddressService {
    List<AddressDTO> getAllAddress();
}
