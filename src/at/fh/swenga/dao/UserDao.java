package at.fh.swenga.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import at.fh.swenga.model.User;

@Repository
@Transactional
public class UserDao {

	@PersistenceContext
	protected EntityManager entityManager;
	
	public List<User> getUsersWithRoles() {
		TypedQuery<User> typedQuery = entityManager.createQuery("SELECT  u.username, ur.role FROM user u JOIN u.id ur WHERE ur.id= :id", User.class);
		List<User> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}

	public User getUserByUserName(String userName) {
		TypedQuery<User> typedQuery = entityManager.createQuery("SELECT u FROM User u WHERE u.userName = :name",
				User.class);
		typedQuery.setParameter("name", userName);
		typedQuery.setMaxResults(1);
		User user = typedQuery.getSingleResult();

		return user;
	}

	public String getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		return username;
	}

	public User getUserById(int i) throws DataAccessException {
		return entityManager.find(User.class, i);
	}

	public List<User> getUsers() {
		TypedQuery<User> typedQuery = entityManager.createQuery("SELECT u FROM User u", User.class);
		List<User> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}

	public void delete(int id) {
		User user = getUserById(id);
		if (user != null) {
			delete(user);
		}
	}

	public void delete(User user) {
		entityManager.remove(user);
	}

	public void persist(User user) {
		entityManager.persist(user);
	}

	public User merge(User user) {
		return entityManager.merge(user);
	}
}
