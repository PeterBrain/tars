package at.fh.swenga.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import at.fh.swenga.model.User;

@Repository
@Transactional
public class UserDao {

	@PersistenceContext
	protected EntityManager entityManager;

	public List<User> findByUsername(String userName) {
		TypedQuery<User> typedQuery = entityManager.createQuery("SELECT u FROM User u WHERE u.userName = :name",
				User.class);
		typedQuery.setParameter("name", userName);
		List<User> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}

	public void persist(User user) {
		entityManager.persist(user);
	}
	
	public String getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName().toUpperCase();
		
		return username;
	}
}
