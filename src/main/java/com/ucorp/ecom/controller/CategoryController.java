package com.ucorp.ecom.controller;

import com.ucorp.ecom.config.AppConstants;
import com.ucorp.ecom.model.Category;
import com.ucorp.ecom.payload.CategoryDTO;
import com.ucorp.ecom.payload.CategoryDTOResponse;
import com.ucorp.ecom.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Tag(name = "Category APIs",description = "APIs for managing categories")
    @Operation(summary = "Get Category",description = "API to get all categories")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "get categories"),
            @ApiResponse(responseCode = "400",description = "Invalid Input", content = @Content),
            @ApiResponse(responseCode = "500",description = "Interval server error",content = @Content)
    })
    @GetMapping("/public/categories")
    public ResponseEntity<CategoryDTOResponse> getAllCategories(
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false)Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_CATEGORIES_BY,required = false)String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_DIR,required = false)String sortOrder) {
        CategoryDTOResponse categoryDTOResponse = categoryService.getAllCategories(pageNumber, pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(categoryDTOResponse, HttpStatus.OK);
    }

    @Tag(name = "Category APIs",description = "APIs for managing categories")
    @Operation(summary = "Create categories",description = "APIs to create categories")
    @ApiResponses({
            @ApiResponse(responseCode = "201",description = "Create category succesfully"),
            @ApiResponse(responseCode = "400",description = "Invalid input",content = @Content),
            @ApiResponse(responseCode = "500",description = "Internal Server error",content = @Content)
    })
    @PostMapping("/public/categories")
    public ResponseEntity<CategoryDTO> addCategory(@Parameter(name = "this") @Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO savedCategoryDTO = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(savedCategoryDTO,HttpStatus.CREATED);
    }

    @Tag(name = "Category APIs",description = "APIs for managing categories")
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId) {
        CategoryDTO  deletedCategory = categoryService.deleteCategory(categoryId);
        return  ResponseEntity.status(HttpStatus.OK).body(deletedCategory);
    }

    @Tag(name = "Category APIs",description = "APIs for managing categories")
    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO categoryDTO,@PathVariable Long categoryId) {
            CategoryDTO updateCategory = categoryService.updateCategory(categoryDTO,categoryId);
            return new ResponseEntity<>(updateCategory ,HttpStatus.OK);
    }

}
