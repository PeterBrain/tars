package at.fh.swenga.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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

	public List<Entry> getAllEntriesOfUser(int id) {
		Query query = entityManager.createNamedQuery("Entry.getAllEntriesFromUser");
		query = query.setParameter("id", id);
		List<Entry> entries = query.getResultList();
		return entries;
	}

	public List<Entry> getEntries() {
		String currentUserName = userDao.getCurrentUser();
		User currentUser = userDao.getUserByUserName(currentUserName);

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
	
	public List<Entry> getEntriesLastWeek() {
		String currentUserName = userDao.getCurrentUser();
		User currentUser = userDao.getUserByUserName(currentUserName);

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
			
			Date now = new Date();
			
			List<Entry> resultList = new ArrayList<>();
			
			for (Entry entry : resultListAll) {
				if (entry.getTimestampStart().after(java.sql.Date.valueOf(LocalDate.now().minusDays(7)))) {
					resultList.add(entry);
				}	
			}
			
			return resultList;
		}
	}

	public List<Entry> searchEntries(String searchString) {
		TypedQuery<Entry> typedQuery = entityManager.createQuery(
				"SELECT e FROM Entry e WHERE e.firstName LIKE :search or e.lastName LIKE :search", Entry.class);
		typedQuery.setParameter("search", "%" + searchString + "%");
		List<Entry> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}

	public Entry getEntryById(int i) throws DataAccessException {
		return entityManager.find(Entry.class, i);
	}

	public void persist(Entry entry) {
		entityManager.persist(entry);
	}

	public Entry merge(Entry entry) {
		return entityManager.merge(entry);
	}

	@Secured("ROLE_ADMIN")
	public void delete(Entry entry) {
		entityManager.remove(entry);
	}

	@Secured("ROLE_ADMIN")
	public int deleteAllEntries() {
		int count = entityManager.createQuery("DELETE FROM Entry").executeUpdate();
		return count;
	}

	@Secured("ROLE_ADMIN")
	public void delete(int id) {
		Entry entry = getEntryById(id);
		if (entry != null) {
			delete(entry);
		}
	}
}