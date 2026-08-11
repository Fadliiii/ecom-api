package com.ucorp.ecom.controller;

import com.ucorp.ecom.model.User;
import com.ucorp.ecom.payload.AddressDTO;
import com.ucorp.ecom.service.AddresService;
import com.ucorp.ecom.util.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    private AddresService addresService;

    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO) {
       User user = authUtil.loogedInUser();
       AddressDTO dto = addresService.createAddress(addressDTO,user);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAllAddresses() {
       List<AddressDTO> addressDTOList = addresService.getAllAddress();
        return ResponseEntity.ok(addressDTOList);
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId) {
        AddressDTO addressDTO = addresService.getAddressById(addressId);
        return ResponseEntity.ok(addressDTO);
    }

    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressDTO>> getAllAddressesByUser() {
        User user = authUtil.loogedInUser();
        List<AddressDTO> addressDTOList = addresService.getUserAddress(user);

        return ResponseEntity.ok(addressDTOList);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddressById(@PathVariable Long addressId, @Valid @RequestBody AddressDTO addressDTO) {
        AddressDTO updatedAddress = addresService.updateAddresByAddressId(addressId,addressDTO);
        return new  ResponseEntity<AddressDTO>(updatedAddress,HttpStatus.OK);
    }
    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> deleteAddressById(@PathVariable Long addressId) {
        String status = addresService.deleteAddress(addressId);
        return new ResponseEntity<>(status,HttpStatus.OK);
    }
}
