package at.fh.swenga.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.fh.swenga.model.Category;

@Repository
@Transactional
public class CategoryDao {
	
	@PersistenceContext
	protected EntityManager entityManager;

	public List<Category> getCategories() {
		TypedQuery<Category> typedQuery = entityManager.createQuery("SELECT c FROM Category c", Category.class);
		List<Category> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}
}
