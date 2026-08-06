package com.ucorp.ecom.service;

import com.ucorp.ecom.exceptions.APIException;
import com.ucorp.ecom.exceptions.ResourceNotFoundException;
import com.ucorp.ecom.model.Cart;
import com.ucorp.ecom.model.CartItem;
import com.ucorp.ecom.model.Product;
import com.ucorp.ecom.payload.CartDTO;
import com.ucorp.ecom.payload.ProductDTO;
import com.ucorp.ecom.repository.CartItemRepository;
import com.ucorp.ecom.repository.CartRepository;
import com.ucorp.ecom.repository.ProductRepository;
import com.ucorp.ecom.util.AuthUtil;
import jakarta.validation.constraints.Size;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartSericeImpl implements CartService{
    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private AuthUtil authUtil;

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        //Find existing cart or create one
        Cart cart = createCart();

        //Retrive Product Details
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product","productId",productId));

        //Perform Validations
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(
                cart.getCartId(),
                productId
        );
        if (cartItem != null) {
            throw new APIException("product"+product.getProductName()+" already exists");
        }
        if (product.getQuantity() == 0) {
            throw new APIException(product.getProductName()+" is not available");
        }
        if (product.getQuantity() < quantity) {
            throw new APIException("Please, make an order of the " + product.getProductName()+
                    "less than or equal to " + product.getQuantity()+".");
        }

        //Create CartItem
        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());


        // Save CartItem
        cartItemRepository.save(newCartItem);

        product.setQuantity(product.getQuantity());
        cart.getCartItem().add(newCartItem);

        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));

        //return updated cart
        cartRepository.save(cart);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<CartItem>cartItems = cart.getCartItem();
        Stream<ProductDTO> productDTOStream = cartItems.stream()
                .map(item-> {
                    ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
                    map.setQuantity(item.getQuantity());
                    return map;
                });
        cartDTO.setProducts(productDTOStream.collect(Collectors.toList()));

        return cartDTO;
    }

    private Cart createCart() {
        Cart userCart = cartRepository.findCartByEmail((authUtil.loogedInEmail()));
        if (userCart != null) {
            return userCart;
        }

        Cart cart = new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loogedInUser());
        Cart newCart = cartRepository.save(cart);
        return newCart;
    }
}
