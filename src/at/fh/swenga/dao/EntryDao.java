package at.fh.swenga.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.fh.swenga.model.Entry;
import at.fh.swenga.model.User;
import at.fh.swenga.model.UserRole;

@Repository
@Transactional
public class EntryDao {

	@PersistenceContext
	protected EntityManager entityManager;

	@Autowired
	UserDao userDao;

	@Autowired
	UserRoleDao userRoleDao;

	/**
	 * Get Entries from a specific User
	 * 
	 * @param id
	 * @return
	 */
//	public List<Entry> getAllEntriesOfUser(int id) {
//		Query query = entityManager.createNamedQuery("Entry.getAllEntriesFromUser");
//		query = query.setParameter("id", id);
//		List<Entry> entries = query.getResultList();
//		return entries;
//	}

	/**
	 * Get all entries by logged in user. Get all entries if admin is logged in.
	 * 
	 * @return
	 */
	public List<Entry> getEntries() {
		// get logged in user
		String currentUserName = userDao.getCurrentUser();
		User currentUser = userDao.getUserByUserName(currentUserName);

		// get admin-role object
		UserRole adminRole = userRoleDao.getRole("ROLE_ADMIN");

		// an admin should see all entries of all users
		if (currentUser.getUserRoles().contains(adminRole)) {
			TypedQuery<Entry> typedQuery = entityManager.createQuery("SELECT e FROM Entry e", Entry.class);
			List<Entry> typedResultList = typedQuery.getResultList();
			return typedResultList;
		} else {

			Query query = entityManager.createNamedQuery("Entry.findByEditor");
			query = query.setParameter("user", currentUser);

			List<Entry> resultList = query.getResultList();
			return resultList;
		}
	}

	/**
	 * Get entries started in the last 7 days. Admin gets all entries. Users get own
	 * entries
	 * 
	 * @return
	 */
	public List<Entry> getEntriesLastWeek() {
		// get current user object
		String currentUserName = userDao.getCurrentUser();
		User currentUser = userDao.getUserByUserName(currentUserName);

		// get userrole object
		UserRole adminRole = userRoleDao.getRole("ROLE_ADMIN");

		// an admin should see all entries of all users
		if (currentUser.getUserRoles().contains(adminRole)) {
			TypedQuery<Entry> typedQuery = entityManager.createQuery("SELECT e FROM Entry e", Entry.class);
			List<Entry> typedResultList = typedQuery.getResultList();
			return typedResultList;
		} else {

			Query query = entityManager.createNamedQuery("Entry.findByEditor");
			query = query.setParameter("user", currentUser);

			List<Entry> resultListAll = query.getResultList();

			// Date now = new Date();

			List<Entry> resultList = new ArrayList<>();

			// add entry to result list if started within last 7 days
			for (Entry entry : resultListAll) {
				if (entry.getTimestampStart().after(java.sql.Date.valueOf(LocalDate.now().minusDays(7)))) {
					resultList.add(entry);
				}
			}

			return resultList;
		}
	}

	/**
	 * search for entry
	 * 
	 * @param searchString
	 * @return
	 */
	public List<Entry> searchEntries(String searchString) {
		TypedQuery<Entry> typedQuery = entityManager.createQuery(
				"SELECT e FROM Entry e WHERE e.firstName LIKE :search or e.lastName LIKE :search", Entry.class);
		typedQuery.setParameter("search", "%" + searchString + "%");
		List<Entry> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}

	/**
	 * get Entry object using id
	 * 
	 * @param i
	 * @return
	 * @throws DataAccessException
	 */
	public Entry getEntryById(int i) throws DataAccessException {
		return entityManager.find(Entry.class, i);
	}

	/**
	 * persist entry
	 * 
	 * @param entry
	 */
	public void persist(Entry entry) {
		entityManager.persist(entry);
	}

	/**
	 * merge entry
	 * 
	 * @param entry
	 * @return
	 */
	public Entry merge(Entry entry) {
		return entityManager.merge(entry);
	}

	/**
	 * delete entry
	 * 
	 * @param entry
	 */
	@Secured("ROLE_ADMIN")
	public void delete(Entry entry) {
		entityManager.remove(entry);
	}

	/**
	 * delete all Entries
	 * 
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	public int deleteAllEntries() {
		int count = entityManager.createQuery("DELETE FROM Entry").executeUpdate();
		return count;
	}

	/**
	 * delete entry
	 * 
	 * @param id
	 */
	@Secured("ROLE_ADMIN")
	public void delete(int id) {
		Entry entry = getEntryById(id);
		if (entry != null) {
			delete(entry);
		}
	}
}