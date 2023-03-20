package com.noithat.WebNoiThat.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.noithat.WebNoiThat.dao.CategoryDao;
import com.noithat.WebNoiThat.entity.Category;
import com.noithat.WebNoiThat.model.CategoryDTO;
import com.noithat.WebNoiThat.service.CategoryService;


@Transactional
@Service
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	private CategoryDao categoryDao;
	
	@Override
	public List<CategoryDTO> findAll() {
		List<Category> categories = categoryDao.findAll();
		List<CategoryDTO> categoryDTOs = new ArrayList<CategoryDTO>();
		for (Category category : categories) {
			CategoryDTO categoryDTO = new CategoryDTO();
			categoryDTO.setCategoryId(category.getCategoryId());
			categoryDTO.setCategoryName(category.getCategoryName());
			categoryDTOs.add(categoryDTO);
		}
		return categoryDTOs;
	}

}
