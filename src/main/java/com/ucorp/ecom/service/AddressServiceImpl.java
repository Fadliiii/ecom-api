package com.ucorp.ecom.service;

import com.ucorp.ecom.exceptions.ResourceNotFoundException;
import com.ucorp.ecom.model.Address;
import com.ucorp.ecom.model.User;
import com.ucorp.ecom.payload.AddressDTO;
import com.ucorp.ecom.repository.AddressRepository;
import com.ucorp.ecom.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddresService{

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {
        Address address = modelMapper.map(addressDTO, Address.class);

        List<Address> addressList =user.getAddresses();
        addressList.add(address);

        user.setAddresses(addressList);

        address.setUser(user);
        Address savedAddress = addressRepository.save(address);

        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAllAddress() {
        List<Address> addressList = addressRepository.findAll();
//        List<AddressDTO> addressDTOList = new ArrayList<>();
//        for (Address address : addressList) {
//            addressDTOList.add(modelMapper.map(address, AddressDTO.class));
//        }
        List<AddressDTO> addressDTOList = addressList.stream().map( address -> {
            AddressDTO dto = modelMapper.map(address, AddressDTO.class);
            return dto;
        }
        ).toList();
        return  addressDTOList;
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("Address", "id", addressId)
        );
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getUserAddress(User user) {
        List<Address> addressList = user.getAddresses();

        return addressList.stream().map(address ->
                modelMapper.map(address, AddressDTO.class)).toList();
    }

    @Override
    public AddressDTO updateAddresByAddressId(Long addressId, AddressDTO addressDTO) {
        Address addressFromDb = addressRepository.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("Address", "id", addressId)
        );
        if (addressDTO.getState() != null && !addressDTO.getState().isBlank()) {
            addressFromDb.setState(addressDTO.getState());
        }
        if (addressDTO.getCity() != null && !addressDTO.getCity().isBlank()) {
            addressFromDb.setCity(addressDTO.getCity());
        }
        if (addressDTO.getCountry() != null && !addressDTO.getCountry().isBlank()) {
            addressFromDb.setCountry(addressDTO.getCountry());
        }
        if (addressDTO.getPincode() != null && !addressDTO.getPincode().isBlank()) {
            addressFromDb.setPincode(addressDTO.getPincode());
        }
        if (addressDTO.getStreet() != null && !addressDTO.getStreet().isBlank()) {
            addressFromDb.setStreet(addressDTO.getStreet());
        }
        if (addressDTO.getBuildingName() != null && !addressDTO.getBuildingName().isBlank()) {
            addressFromDb.setBuildingName(addressDTO.getBuildingName());
        }
       Address updatedAddress = addressRepository.save(addressFromDb);
       User user = updatedAddress.getUser();

       user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
       user.getAddresses().add(updatedAddress);
       userRepository.save(user);

       return modelMapper.map(updatedAddress, AddressDTO.class);
    }

    @Override
    public String deleteAddress(Long addressId) {
        Address addressFromDb = addressRepository.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("Address", "id", addressId)
        );
        User user = addressFromDb.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));

        userRepository.save(user);
        addressRepository.delete(addressFromDb);

        return "Address successfully deleted "+addressId;
    }
}
