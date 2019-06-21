package at.fh.swenga.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.dao.DataAccessException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.fh.swenga.model.SecurityMessage;

@Repository
@Transactional
public class SecurityMessageDao {

	@PersistenceContext
	protected EntityManager entityManager;

	/**
	 * get all Security Messages
	 * 
	 * @return
	 */
	public List<SecurityMessage> getSecurityMessages() {
		TypedQuery<SecurityMessage> typedQuery = entityManager.createQuery("SELECT s FROM SecurityMessage s",
				SecurityMessage.class);
		List<SecurityMessage> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}

	/**
	 * get specific Security Message by id
	 * 
	 * @param i
	 * @return
	 * @throws DataAccessException
	 */
	public SecurityMessage getSecurityMessageById(int i) throws DataAccessException {
		return entityManager.find(SecurityMessage.class, i);
	}

	/**
	 * persist Security Message
	 * 
	 * @param securityMessage
	 */
	public void persist(SecurityMessage securityMessage) {
		entityManager.persist(securityMessage);
	}

	/**
	 * merge Security Message
	 * 
	 * @param securityMessage
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	public SecurityMessage merge(SecurityMessage securityMessage) {
		return entityManager.merge(securityMessage);
	}

	/**
	 * delete Security Message
	 * 
	 * @param securityMessage
	 */
	@Secured("ROLE_ADMIN")
	public void delete(SecurityMessage securityMessage) {
		entityManager.remove(securityMessage);
	}

	/**
	 * delete Security Message
	 * 
	 * @param id
	 */
	@Secured("ROLE_ADMIN")
	public void delete(int id) {
		SecurityMessage securityMessage = getSecurityMessageById(id);
		if (securityMessage != null) {
			delete(securityMessage);
		}
	}
}
