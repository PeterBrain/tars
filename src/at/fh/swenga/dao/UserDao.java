package at.fh.swenga.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import at.fh.swenga.model.User;

@Repository
@Transactional
public class UserDao {

	@PersistenceContext
	protected EntityManager entityManager;

	@Autowired
	UserRoleDao userRoleDao;

	/*
	 * public List<User> getUsersWithRoles() { TypedQuery<User> typedQuery =
	 * entityManager.
	 * createQuery("SELECT u.username, ur.role FROM user u JOIN u.id ur WHERE ur.id= :id"
	 * , User.class); List<User> typedResultList = typedQuery.getResultList();
	 * return typedResultList; }
	 */

	public List<User> getUsersWithRole(int id) {
		Query query = entityManager.createNamedQuery("User.getAllWithRole");
		query = query.setParameter("id", id);
		List<User> users = query.getResultList();
		return users;
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

	@Secured("ROLE_ADMIN")
	public void delete(int id) {
		User user = getUserById(id);
		if (user != null) {
			delete(user);
		}
	}
	@Secured("ROLE_ADMIN")
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
