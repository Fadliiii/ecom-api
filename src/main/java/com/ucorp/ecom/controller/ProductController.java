package com.ucorp.ecom.controller;

import com.ucorp.ecom.config.AppConstants;
import com.ucorp.ecom.model.Product;
import com.ucorp.ecom.payload.ProductDTO;
import com.ucorp.ecom.payload.ProductResponseDTO;
import com.ucorp.ecom.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct( @Valid @RequestBody ProductDTO productDTO,
                                                 @PathVariable Long categoryId) {


     ProductDTO savedProductDTO =  productService.add(categoryId,productDTO);
    return new ResponseEntity<>(savedProductDTO,HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponseDTO> getProduct(@RequestParam(defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                                                         @RequestParam(defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
                                                         @RequestParam(defaultValue = AppConstants.SORT_PRODUCT_BY,required = false)String sortBy,
                                                         @RequestParam(defaultValue = AppConstants.SORT_DIR,required = false) String sortOrder) {
       ProductResponseDTO productResponseDTO =  productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponseDTO> getProductByCategory(@PathVariable Long categoryId,
                                                                   @RequestParam(defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                                                                   @RequestParam(defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
                                                                   @RequestParam(defaultValue = AppConstants.SORT_PRODUCT_BY,required = false)String sortBy,
                                                                   @RequestParam(defaultValue = AppConstants.SORT_DIR,required = false) String sortOrder) {
        ProductResponseDTO productResponseDTO = productService.searchByCategory(categoryId,pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponseDTO> getProductByKeyword( @PathVariable String keyword,
                                                                   @RequestParam(defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                                                                   @RequestParam(defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
                                                                   @RequestParam(defaultValue = AppConstants.SORT_PRODUCT_BY,required = false)String sortBy,
                                                                   @RequestParam(defaultValue = AppConstants.SORT_DIR,required = false) String sortOrder) {
        ProductResponseDTO productResponseDTO = productService.searchProductByKeyword(keyword,pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productResponseDTO, HttpStatus.FOUND);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @PathVariable Long productId,@RequestBody ProductDTO productDTO) {
        ProductDTO savedProductDTO = productService.updateProducts(productId,productDTO);
        return  new ResponseEntity<>(savedProductDTO,HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId) {
        ProductDTO productDTO = productService.deleteProduct(productId);
        return new ResponseEntity<>(productDTO,HttpStatus.OK);
    }
    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId, @RequestParam("image") MultipartFile image) throws IOException {
           ProductDTO updateProduct = productService.updateProductImage(productId,image);
            return new ResponseEntity<>(updateProduct,HttpStatus.OK);
    }

}
