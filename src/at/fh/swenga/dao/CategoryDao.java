package at.fh.swenga.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.fh.swenga.model.Category;

@Repository
@Transactional
public interface CategoryDao extends JpaRepository<Category, Integer> {

	/*
	 * @PersistenceContext protected EntityManager entityManager;
	 * 
	 * public List<Category> getCategories() { TypedQuery<Category> typedQuery =
	 * entityManager.createQuery("SELECT c FROM Category c", Category.class);
	 * List<Category> typedResultList = typedQuery.getResultList(); return
	 * typedResultList; }
	 * 
	 * public Category getCategoryById(int i) throws DataAccessException { return
	 * entityManager.find(Category.class, i); }
	 * 
	 * public void persist(Category category) { entityManager.persist(category); }
	 * 
	 * @Secured("ROLE_ADMIN") public Category merge(Category category) { return
	 * entityManager.merge(category); }
	 * 
	 * @Secured("ROLE_ADMIN") public void delete(Category category) {
	 * entityManager.remove(category); }
	 * 
	 * @Secured("ROLE_ADMIN") public void delete(int id) { Category category =
	 * getCategoryById(id); if (category != null) { delete(category); } }
	 */
}
