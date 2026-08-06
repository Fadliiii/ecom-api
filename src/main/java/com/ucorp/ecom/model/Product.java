package com.ucorp.ecom.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;

    @Size(min = 3,message = "minimum 3 character")
    private String productName;

    @Size(min = 6, message = "minimum charcater is 6")
    private String description;

    private Integer quantity;

    private double price;

    private Double specialPrice;

    private Double discount;

    private String image;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User user;

    @OneToMany(mappedBy = "product",cascade = {CascadeType.PERSIST,CascadeType.MERGE}
    ,fetch = FetchType.EAGER)
    private List<CartItem>product= new ArrayList<>();
}
