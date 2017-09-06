package com.library.app.category.services.impl;

import static com.library.app.commontests.category.CategoryForTestsRepository.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;

import com.library.app.category.exception.CategoryExistentException;
import com.library.app.category.exception.CategoryNotFoundException;
import com.library.app.category.model.Category;
import com.library.app.category.repository.CategoryRepository;
import com.library.app.category.services.CategoryServices;
import com.library.app.common.exception.FieldNotValidException;


public class CategoryServiceUTest {
	private CategoryServices categoryServices;
	private Validator validator;
	private CategoryRepository categoryRepository;
	
	@Before
	public void initTestCase() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
		categoryRepository = mock(CategoryRepository.class);
		
		categoryServices = new CategoryServicesImpl();
		((CategoryServicesImpl)categoryServices).validator = validator;
		((CategoryServicesImpl)categoryServices).categoryRepository = categoryRepository;
	}

	@Test
	public void addCategoryWithNullName() {
		addCategoryWithInvalidName(null);
	}
	
	@Test
	public void addCategoryWithShortName() {
		addCategoryWithInvalidName("S");
	}
	
	@Test
	public void addCategoryWithLongName() {
		addCategoryWithInvalidName("This name is to long for the Category and the exception will be thrown");
	}
	
	@Test(expected = CategoryExistentException.class)
	public void addCategoryWithExistentName() {
		when(categoryRepository.alreadyExists(java())).thenReturn(true);
		categoryServices.add(java());
	}
	
	@Test
	public void addValidCategory() {
		when(categoryRepository.alreadyExists(java())).thenReturn(false);
		when(categoryRepository.add(java())).thenReturn(categoryWithId(java(),1L));
		
		final Category categoryAdd = categoryServices.add(java());
		assertThat(categoryAdd.getId(), is(equalTo(1L)));
	}
	
	@Test
	public void updateCategoryWithNullName() {
		updateCategoryWithInvalidName(null);
	}
	
	@Test
	public void updateCategoryWithShortName() {
		updateCategoryWithInvalidName("S");
	}
	
	@Test
	public void updateCategoryWithLongName() {
		updateCategoryWithInvalidName("This name is to long for the Category and the exception will be thrown");
	}
	
	@Test(expected = CategoryExistentException.class)
	public void updateCategoryWithExistentName() {
		when(categoryRepository.alreadyExists(categoryWithId(java(), 1L))).thenReturn(true);
		
		categoryServices.update(categoryWithId(java(), 1L));
	}
	
	@Test(expected = CategoryNotFoundException.class)
	public void updateCategoryNotFound(){
		when(categoryRepository.alreadyExists(categoryWithId(java(), 1L))).thenReturn(false);
		when(categoryRepository.alreadyExistsById(1L)).thenReturn(false);
		
		categoryServices.update(categoryWithId(java(), 1L));
	}
	
	@Test
	public void updateValidCategory() {
		when(categoryRepository.alreadyExists(categoryWithId(java(), 1L))).thenReturn(false);
		when(categoryRepository.alreadyExistsById(1L)).thenReturn(true);

		categoryServices.update(categoryWithId(java(), 1L));
		
		verify(categoryRepository).update(categoryWithId(java(), 1L));
	}
	
	@Test
	public void findCategoryById() {
		when(categoryRepository.findCategoryById(1L)).thenReturn(categoryWithId(java(), 1L));

		assertThat(categoryServices.find(1L), is(equalTo(categoryWithId(java(), 1L))));
	}
	
	@Test(expected = CategoryNotFoundException.class)
	public void findCategoryByIdNotFound() {
		when(categoryRepository.findCategoryById(1L)).thenReturn(null);
		categoryServices.find(1L);
	}
	
	@Test
	public void findAllNoCategories() {
		when(categoryRepository.findAll("name")).thenReturn(new ArrayList<>());
		
		final List<Category> categoryList = categoryServices.findAll("name");
		assertThat(categoryList.isEmpty(), is(equalTo(true)));
	}

	@Test
	public void findAllCategories() {
		when(categoryRepository.findAll("name")).thenReturn(new ArrayList<>(Arrays.asList(java(), cleanCode())));
		
		final List<Category> categoryList = categoryServices.findAll("name");
		assertThat(categoryList.isEmpty(), is(equalTo(false)));
		assertThat(categoryList.size(), is(equalTo(2)));
		assertThat(categoryList.get(0).getName(), is(equalTo(java().getName())));
		assertThat(categoryList.get(1).getName(), is(equalTo(cleanCode().getName())));
		
	}
	
	public void addCategoryWithInvalidName(String name) {
		try {
			categoryServices.add(new Category(name));
			fail("Error should have been thrown");
		}catch(FieldNotValidException e){
			assertThat(e.getFieldName(), is(equalTo("name")));
		}
	}

	public void updateCategoryWithInvalidName(String name) {
		try {
			categoryServices.update(new Category(name));
			fail("Error should have been thrown");
		}catch(FieldNotValidException e){
			assertThat(e.getFieldName(), is(equalTo("name")));
		}
	}

}
