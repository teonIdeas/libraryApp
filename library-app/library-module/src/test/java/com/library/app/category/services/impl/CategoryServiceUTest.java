package com.library.app.category.services.impl;

import static com.library.app.commontests.category.CategoryForTestsRepository.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;

import com.library.app.category.exception.CategoryExistentException;
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
	
	


	public void addCategoryWithInvalidName(String name) {
		try {
			categoryServices.add(new Category(name));
			fail("Error should have been thrown");
		}catch(FieldNotValidException e){
			assertThat(e.getFieldName(), is(equalTo("name")));
		}
	}
}
