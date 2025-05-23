package com.springboot.blog.service.impl;

import com.springboot.blog.dto.CategoryDto;
import com.springboot.blog.entity.Category;
import com.springboot.blog.exception.BlogApiException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.service.CategoryService;
import com.springboot.blog.utils.BlogMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    private CategoryRepository categoryRepository;


    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {

        Category category=categoryRepository.save(BlogMapper.toCategory(categoryDto));

        return BlogMapper.toCategoryDto(category);
    }

    @Override
    public CategoryDto getCategory(Long categoryId) {

      Category category= categoryRepository.findById(categoryId)
               .orElseThrow(() -> new ResourceNotFoundException("Category","id",categoryId));

        return BlogMapper.toCategoryDto(category);
    }

    @Override
    public List<CategoryDto> getAllCategories() {

        List<Category> categoryList=categoryRepository.findAll();

        return categoryList.stream().map(BlogMapper::toCategoryDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto updateCategory(Long categoryId,CategoryDto categoryDto) {

        Category category=categoryRepository.findById(categoryId).
                orElseThrow(()->new ResourceNotFoundException("Category","id",categoryId));

        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());

        return BlogMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public String deleteCategory(Long categoryId) {

        categoryRepository.findById(categoryId)
                        .orElseThrow(()->new ResourceNotFoundException("Category","id",categoryId));

        categoryRepository.deleteById(categoryId);

        return "Deleted Category Successfully with id "+categoryId;
    }
}
