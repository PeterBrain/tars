package at.fh.swenga.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.fh.swenga.model.SecurityMessage;

@Repository
@Transactional
public class SecurityMessageDao {

	protected EntityManager entityManager;

	public List<SecurityMessage> getSecurityMessages() {
		TypedQuery<SecurityMessage> typedQuery = entityManager.createQuery("SELECT s FROM SecurityMessage s",
				SecurityMessage.class);
		List<SecurityMessage> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}
}
