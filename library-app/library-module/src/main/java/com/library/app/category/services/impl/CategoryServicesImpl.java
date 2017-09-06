package com.library.app.category.services.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.library.app.category.exception.CategoryExistentException;
import com.library.app.category.exception.CategoryNotFoundException;
import com.library.app.category.model.Category;
import com.library.app.category.repository.CategoryRepository;
import com.library.app.category.services.CategoryServices;
import com.library.app.common.exception.FieldNotValidException;

public class CategoryServicesImpl implements CategoryServices {

	Validator validator;
	CategoryRepository categoryRepository;
	
	@Override
	public Category add(final Category category){
		validateCategory(category);
		return categoryRepository.add(category);
	}

	@Override
	public void update(Category category) throws FieldNotValidException {
		validateCategory(category);
		
		if(!categoryRepository.alreadyExistsById(category.getId())) {
			throw new CategoryNotFoundException();
		}
		
		categoryRepository.update(category);
		
	}

	@Override
	public Category find(long categoryId){
		Category foundCategory = categoryRepository.findCategoryById(categoryId);
		if(foundCategory == null) {
			throw new CategoryNotFoundException();
		}
		return foundCategory;
	}

	@Override
	public List<Category> findAll(String sortField) {
		return categoryRepository.findAll(sortField);
	}

	private void validateCategory(final Category category) {
		validateCategoryFields(category);
		if(categoryRepository.alreadyExists(category)) {
			throw new CategoryExistentException();
		}
	}

	private void validateCategoryFields(final Category category) {
		Set<ConstraintViolation<Category>> errors = validator.validate(category);
		Iterator<ConstraintViolation<Category>> itErrors = errors.iterator();
		if(itErrors.hasNext()) {
			ConstraintViolation<Category> violation = itErrors.next();
			throw new FieldNotValidException(violation.getPropertyPath().toString(), violation.getMessage());
		}
	}

}
