package com.mobile2016.backend.service;

import com.mobile2016.backend.model.Category;
import com.mobile2016.backend.service.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

	@Autowired
	private CategoryMapper categoryMapper;

	public  List<Category> list(Category category) {
		return categoryMapper.list(category);
	}

	public Integer count(){
		return categoryMapper.count();
	}

	public void delById(Category category){
		categoryMapper.updateState(category);
	}

	public Category findById(int id){
		return categoryMapper.findById(id);
	}

	public void insert(Category category){
		categoryMapper.insert(category);
	}

	public void updateCategory(Category category){
		categoryMapper.updateCategory(category);
	}

}
