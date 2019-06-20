package at.fh.swenga.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.fh.swenga.model.SecurityMessage;

@Repository
@Transactional
public class SecurityMessageDao {

	@PersistenceContext
	protected EntityManager entityManager;

	public List<SecurityMessage> getSecurityMessages() {
		TypedQuery<SecurityMessage> typedQuery = entityManager.createQuery("SELECT s FROM SecurityMessage s",
				SecurityMessage.class);
		List<SecurityMessage> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}

	public SecurityMessage getSecurityMessageById(int i) throws DataAccessException {
		return entityManager.find(SecurityMessage.class, i);
	}
//
//	public void persist(Category category) {
//		entityManager.persist(category);
//	}
//
//	@Secured("ROLE_ADMIN")
//	public Category merge(Category category) {
//		return entityManager.merge(category);
//	}
//
//	@Secured("ROLE_ADMIN")
//	public void delete(Category category) {
//		entityManager.remove(category);
//	}
//
//	@Secured("ROLE_ADMIN")
//	public void delete(int id) {
//		Category category = getCategoryById(id);
//		if (category != null) {
//			delete(category);
//		}
//	}
}
