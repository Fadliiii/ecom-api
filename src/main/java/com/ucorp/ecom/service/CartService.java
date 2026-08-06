package com.ucorp.ecom.service;

import com.ucorp.ecom.payload.CartDTO;

public interface CartService {
    CartDTO addProductToCart(Long productId, Integer quantity);
}
