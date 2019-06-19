package at.fh.swenga.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import at.fh.swenga.model.Entry;
import at.fh.swenga.model.UserRole;

@Repository
@Transactional
public class UserRoleDao {

	@PersistenceContext
	protected EntityManager entityManager;

	public List<UserRole> getRoles() {
		TypedQuery<UserRole> typedQuery = entityManager.createQuery("SELECT ur FROM UserRole ur", UserRole.class);
		List<UserRole> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}

	public UserRole getRole(String roleName) {
		try {
			TypedQuery<UserRole> typedQuery = entityManager
					.createQuery("SELECT ur FROM UserRole ur WHERE ur.roleName = :roleName", UserRole.class);
			typedQuery.setParameter("roleName", roleName);
			return typedQuery.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public UserRole getRoleById(int i) throws DataAccessException {
		return entityManager.find(UserRole.class, i);
	}

	public void persist(UserRole userRole) {
		entityManager.persist(userRole);
	}
}