package com.library.app.category.repository;

import com.library.app.commontests.utils.*;
import static com.library.app.commontests.category.CategoryForTestsRepository.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.library.app.category.model.Category;

public class CategoryRepositoryUTest {
	private EntityManagerFactory emf;
	private EntityManager em;
	private CategoryRepository categoryRepository;
	private DBCommandTransactionalExecutor dbCommandTransactionalExecutor;

	@Before
	public void initTestCase() {
		emf = Persistence.createEntityManagerFactory("libraryPU");
		em = emf.createEntityManager();
		dbCommandTransactionalExecutor = new DBCommandTransactionalExecutor(em);
		categoryRepository = new CategoryRepository();
		categoryRepository.em = em;
	}

	@After
	public void closeEntityManager() {
		em.close();
		emf.close();
	}

	@Test
	public void addCategoryAndFindIt() {
		Long categoryId = dbCommandTransactionalExecutor.executeCommand(() -> {
				return categoryRepository.add(java()).getId();
		});
		
		assertThat(categoryId, is(notNullValue()));
		
		final Category category = categoryRepository.findCategoryById(categoryId);
		assertThat(category, is(notNullValue()));
		assertThat(category.getName(), is(equalTo(java().getName())));
	}
	
	@Test
	public void findCategoryByIdNotFound() {
		final Category category = categoryRepository.findCategoryById(999L);
		assertThat(category, is(nullValue()));
	}
	
	@Test
	public void findCategoryByIdNullValue() {
		final Category category = categoryRepository.findCategoryById(null);
		assertThat(category, is(nullValue()));
	}
	
	@Test
	public void updateCategory() {
		final Long categoryAddedId = dbCommandTransactionalExecutor.executeCommand(() -> {
				return categoryRepository.add(java()).getId();
		});
		
		Category categoryAfterAdd = categoryRepository.findCategoryById(categoryAddedId);
		assertThat(categoryAfterAdd.getName(), is(java().getName()));

		categoryAfterAdd.setName(cleanCode().getName());
		dbCommandTransactionalExecutor.executeCommand(() -> {
				categoryRepository.update(categoryAfterAdd);
				return null;
		});
		
		Category categoryAfterUpdate = categoryRepository.findCategoryById(categoryAddedId);
		assertThat(categoryAfterUpdate.getName(), is(cleanCode().getName()));
	}
	
	@Test
	public void findAllCategories() {
		dbCommandTransactionalExecutor.executeCommand(() -> {
			allCategories().forEach(categoryRepository::add);
			return null;
		});
		
		List<Category> allCategories = categoryRepository.findAll("name");
		assertThat(allCategories.size(), equalTo(4));
		assertThat(allCategories.get(0).getName(), is(equalTo(android().getName())));
		assertThat(allCategories.get(1).getName(), is(equalTo(cleanCode().getName())));
		assertThat(allCategories.get(2).getName(), is(equalTo(java().getName())));
		assertThat(allCategories.get(3).getName(), is(equalTo(network().getName())));
	}
	
	@Test
	public void alreadyExistsForAdd() {
		dbCommandTransactionalExecutor.executeCommand(() -> {
			categoryRepository.add(java());
			return null;
		});
		
		assertThat(categoryRepository.alreadyExists(java()), is(equalTo(true)));
		assertThat(categoryRepository.alreadyExists(cleanCode()), is(equalTo(false)));
	}
	
	@Test
	public void alreadyExistsCategoryWithId() {
		Category java = dbCommandTransactionalExecutor.executeCommand(() -> {
			categoryRepository.add(cleanCode());
			return categoryRepository.add(java());
		});
		
		assertThat(categoryRepository.alreadyExists(java), is(equalTo(false)));
		
		java.setName(cleanCode().getName());
		assertThat(categoryRepository.alreadyExists(java), is(equalTo(true)));
		
		java.setName(android().getName());
		assertThat(categoryRepository.alreadyExists(java), is(equalTo(false)));
	}
	
	@Test
	public void existsById() {
		final Long categoryAddedId = dbCommandTransactionalExecutor.executeCommand(() -> {
			return categoryRepository.add(java()).getId();
		});
		
		assertThat(categoryRepository.alreadyExistsById(categoryAddedId), is(equalTo(true)));
		assertThat(categoryRepository.alreadyExistsById(999L), is(equalTo(false)));
		
	}
	
}
