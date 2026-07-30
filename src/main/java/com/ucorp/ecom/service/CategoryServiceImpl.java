package com.ucorp.ecom.service;

import com.ucorp.ecom.exceptions.APIException;
import com.ucorp.ecom.exceptions.ResourceNotFoundException;
import com.ucorp.ecom.model.Category;
import com.ucorp.ecom.payload.CategoryDTO;
import com.ucorp.ecom.payload.CategoryDTOResponse;
import com.ucorp.ecom.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {


    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryDTOResponse getAllCategories(Integer pageNumber, Integer pageSize,String sortBy, String sortOrder){
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable  pageDetails = PageRequest.of(pageNumber, pageSize,sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);
        List<Category> categories = categoryPage.getContent();
        if (categories.isEmpty()) {
           throw new APIException("No categories found");
       }
       List<CategoryDTO> categoryDTOS = categories.stream()
               .map(category -> modelMapper.map(category, CategoryDTO.class))
               .toList();
       CategoryDTOResponse categoryDTOResponse = new CategoryDTOResponse();
       categoryDTOResponse.setContent(categoryDTOS);
       categoryDTOResponse.setPageNumber(categoryPage.getNumber());
       categoryDTOResponse.setPageSize(categoryPage.getSize());
       categoryDTOResponse.setTotalElements(categoryPage.getTotalElements());
       categoryDTOResponse.setTotalPages(categoryPage.getTotalPages());
       categoryDTOResponse.setLastPage(categoryPage.isLast());
       return categoryDTOResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);

        Category categoryFromDb = categoryRepository.findByCategoryName(category.getCategoryName());
        if (categoryFromDb != null) {
            throw new APIException("Category with the name = "+ category.getCategoryName()+" already exists");
        }
        Category categorySaved = categoryRepository.save(category);
        return modelMapper.map(categorySaved, CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));
        categoryRepository.delete(category);
        return  modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
        //check if exist
        Category savedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));

        //map dto to entity
        Category category = modelMapper.map(categoryDTO, Category.class);

        category.setCategoryId(categoryId);
        savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

}
