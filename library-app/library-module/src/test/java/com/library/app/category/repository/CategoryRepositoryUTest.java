package com.library.app.category.repository;

import com.library.app.commontests.utils.*;
import static com.library.app.commontests.category.CategoryForTestsRepository.java;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

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
}
