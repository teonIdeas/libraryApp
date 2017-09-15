package com.library.app.category.resource;

import static com.library.app.commontests.category.CategoryForTestsRepository.*;
import static org.mockito.Mockito.*;

import javax.ws.rs.core.Response;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.library.app.category.services.CategoryServices;;

public class CategoryResourceUTest {

	private CategoryResource categoryResource;
	
	@Mock
	private CategoryServices categoryServices;
	
	@Before
	public void initTestCase() {
		MockitoAnnotations.initMocks(categoryServices);
		categoryResource = new CategoryResource();
		
		categoryResource.categoryServices = categoryServices;
	}
	
	@Test
	public void addCategory() {
		when(categoryServices.add(java())).thenReturn(categoryWithId(java(), 1L));
		
		Response response = categoryResource.add("readFile");
	}
}
