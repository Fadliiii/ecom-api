package com.ucorp.ecom.service;

import com.ucorp.ecom.model.User;
import com.ucorp.ecom.payload.AddressDTO;

import java.util.List;

public interface AddresService {

    AddressDTO createAddress(AddressDTO addressDTO, User user);

    List<AddressDTO> getAllAddress();

    AddressDTO getAddressById(Long addressId);

    List<AddressDTO> getUserAddress(User user);

    AddressDTO updateAddresByAddressId(Long addressId, AddressDTO addressDTO);

    String deleteAddress(Long addressId);
}
