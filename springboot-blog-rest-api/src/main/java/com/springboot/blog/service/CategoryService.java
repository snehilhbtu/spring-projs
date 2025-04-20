package com.springboot.blog.service;

import com.springboot.blog.dto.CategoryDto;

import java.util.List;
import java.util.Locale;

public interface CategoryService {

    CategoryDto addCategory(CategoryDto categoryDto);

    CategoryDto getCategory(Long categoryId);

    List<CategoryDto> getAllCategories();

}
