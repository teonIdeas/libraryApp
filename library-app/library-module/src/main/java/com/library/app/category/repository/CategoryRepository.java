package com.library.app.category.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.library.app.category.model.Category;

public class CategoryRepository {

	EntityManager em;

	public Category add(final Category category) {
		em.persist(category);
		return category;
	}

	public Category findCategoryById(final Long categoryId) {
		if(categoryId == null) {
			return null;
		}
		return em.find(Category.class, categoryId);
	}

	public Category update(Category categoryAfterAdd) {
		return em.merge(categoryAfterAdd);
	}

	@SuppressWarnings("unchecked")
	public List<Category> findAll(String orderField) {
		return em.createQuery("SELECT e FROM Category e ORDER BY e." + orderField).getResultList();
	}

	public boolean alreadyExists(Category category) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT 1 FROM Category e WHERE e.name = :name");
		
		if(category.getId() != null) {
			jpql.append(" AND e.id != :id");
		}
		
		Query query = em.createQuery(jpql.toString());
		query.setParameter("name", category.getName());
		if(category.getId() != null) {
			query.setParameter("id", category.getId());
		}		
		return query.setMaxResults(1).getResultList().size() > 0;
	}

	public Object alreadyExistsById(Long categoryAddedId) {
		return em.createQuery("SELECT 1 FROM Category e WHERE e.id = :id")
				.setParameter("id", categoryAddedId)
				.setMaxResults(1)
				.getResultList().size()>0;
	}
}
