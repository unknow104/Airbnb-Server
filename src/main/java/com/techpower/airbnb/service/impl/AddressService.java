package com.techpower.airbnb.service.impl;

import com.techpower.airbnb.converter.AddressConverter;
import com.techpower.airbnb.dto.AddressDTO;
import com.techpower.airbnb.repository.AddressRepository;
import com.techpower.airbnb.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService implements IAddressService {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AddressConverter addressConverter;
    @Override
    public List<AddressDTO> getAllAddress() {
        return addressConverter.mapperList(addressRepository.findAll());
    }
}
