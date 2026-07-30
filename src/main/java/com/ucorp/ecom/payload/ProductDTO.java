package com.ucorp.ecom.payload;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long productId;
    @Size(min = 3, message = "minimum charcater is 3")
    private String productName;
    private String image;
    @Size(min = 6, message = "minimum charcater is 6")
    private String description;
    private Integer quantity;
    private Double price;
    private Double discount;
    private Double specialPrice;
}
