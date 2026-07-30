package com.ucorp.ecom.service;

import com.ucorp.ecom.payload.ProductDTO;
import com.ucorp.ecom.payload.ProductResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface ProductService {

    ProductDTO add(Long categoryId, ProductDTO product);

    ProductResponseDTO getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponseDTO searchByCategory(Long categoryId,int pageNumber, int pageSize, String sortBy, String sortOrder);

    ProductResponseDTO searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductDTO updateProducts(Long productId, ProductDTO product);

    ProductDTO deleteProduct(Long productId);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
