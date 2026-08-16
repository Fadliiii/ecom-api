package com.ucorp.ecom.service;

import com.ucorp.ecom.payload.OrderDTO;
import jakarta.transaction.Transactional;

@Transactional
public interface OrderService {
    OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage);
}
