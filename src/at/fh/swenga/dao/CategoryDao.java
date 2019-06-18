package at.fh.swenga.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.fh.swenga.model.Category;
import at.fh.swenga.model.Project;

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
	
	public Category getCategoryById(int i) throws DataAccessException {
		return entityManager.find(Category.class, i);
	}
	
	public void persist(Category category) {
		entityManager.persist(category);
	}
	
	public Category merge(Category category) {
		return entityManager.merge(category);
	}
	
	public void delete(Category category) {
		entityManager.remove(category);
	}
	
	public void delete(int id) {
		Category category = getCategoryById(id);
		if (category != null) {
			delete(category);
		}
	}
}
