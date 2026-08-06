package com.ucorp.ecom.controller;

import com.ucorp.ecom.exceptions.ResourceNotFoundException;
import com.ucorp.ecom.model.Cart;
import com.ucorp.ecom.payload.CartDTO;
import com.ucorp.ecom.repository.CartRepository;
import com.ucorp.ecom.service.CartService;
import com.ucorp.ecom.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private CartRepository cartRepository;

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId,
                                                    @PathVariable Integer quantity) {
        CartDTO cartDTO = cartService.addProductToCart(productId,quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }


    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getCarts() {
        List<CartDTO>cartDTOS = cartService.getAllCarts();
        return new ResponseEntity<List<CartDTO>>(cartDTOS, HttpStatus.FOUND);
    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO>getCartById(){
        String emailId = authUtil.loogedInEmail();
        Cart cart = cartRepository.findCartByEmail(emailId);
        if(cart==null){
            throw new ResourceNotFoundException("Cart","emailId",emailId);
        }
        Long cartId =cart.getCartId();
       CartDTO cartDTO =cartService.getCart(emailId,cartId);
        return ResponseEntity.ok(cartDTO);
    }

    @PutMapping("/carts/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateCart(@PathVariable Long productId,
                                              @PathVariable String operation) {
        
    }
}
