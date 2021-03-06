package at.fh.swenga.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

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

	/**
	 * get all users with a specific role
	 * 
	 * @param id
	 * @return
	 */
	public List<User> getUsersWithRole(int id) {
		Query query = entityManager.createNamedQuery("User.getAllWithRole");
		query = query.setParameter("id", id);
		List<User> users = query.getResultList();
		return users;
	}

	/**
	 * get user object with username
	 * 
	 * @param userName
	 * @return
	 */
	public User getUserByUserName(String userName) {
		TypedQuery<User> typedQuery = entityManager.createQuery("SELECT u FROM User u WHERE u.userName = :name",
				User.class);
		typedQuery.setParameter("name", userName);
		typedQuery.setMaxResults(1);
		User user = typedQuery.getSingleResult();

		return user;
	}

	/**
	 * get logged in username
	 * 
	 * @return
	 */
	public String getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		return username;
	}

	/**
	 * get user by id
	 * 
	 * @param i
	 * @return
	 * @throws DataAccessException
	 */
	public User getUserById(int i) throws DataAccessException {
		return entityManager.find(User.class, i);
	}

	/**
	 * get all users
	 * 
	 * @return
	 */
	public List<User> getUsers() {
		TypedQuery<User> typedQuery = entityManager.createQuery("SELECT u FROM User u", User.class);
		List<User> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}

	/**
	 * persist user
	 * 
	 * @param user
	 */
	public void persist(User user) {
		entityManager.persist(user);
	}

	/**
	 * merge user
	 * 
	 * @param user
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	public User merge(User user) {
		return entityManager.merge(user);
	}

//	@Secured("ROLE_ADMIN")
//	public void delete(int id) {
//		User user = getUserById(id);
//		if (user != null) {
//			delete(user);
//		}
//	}
//
//	@Secured("ROLE_ADMIN")
//	public void delete(User user) {
//		entityManager.remove(user);
//	}
}
