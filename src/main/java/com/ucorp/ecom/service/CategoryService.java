package com.ucorp.ecom.service;


import com.ucorp.ecom.model.Category;
import com.ucorp.ecom.payload.CategoryDTO;
import com.ucorp.ecom.payload.CategoryDTOResponse;

import java.util.List;

public interface CategoryService {
    CategoryDTOResponse getAllCategories(Integer pageNumber, Integer pageSize,String SortBy, String sortOrder);
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO deleteCategory(Long categoryId);
    CategoryDTO updateCategory(CategoryDTO categoryDTO,Long categoryId);
}
