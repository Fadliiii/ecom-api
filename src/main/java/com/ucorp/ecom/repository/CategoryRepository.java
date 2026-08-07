package com.ucorp.ecom.repository;

import com.ucorp.ecom.model.Cart;
import com.ucorp.ecom.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    public Category findByCategoryName(String categoryName);


}
