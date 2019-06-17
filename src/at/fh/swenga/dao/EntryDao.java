package at.fh.swenga.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.fh.swenga.model.Entry;

@Repository
@Transactional
public class EntryDao {

	@PersistenceContext
	protected EntityManager entityManager;

	public List<Entry> getEntries() {
		TypedQuery<Entry> typedQuery = entityManager.createQuery("SELECT e FROM Entry e", Entry.class);
		List<Entry> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}

	public List<Entry> searchEntries(String searchString) {
		TypedQuery<Entry> typedQuery = entityManager.createQuery(
				"SELECT e FROM Entry e WHERE e.firstName LIKE :search or e.lastName LIKE :search", Entry.class);
		typedQuery.setParameter("search", "%" + searchString + "%");
		List<Entry> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}

	public Entry getEntry(int i) throws DataAccessException {
		return entityManager.find(Entry.class, i);
	}

	public void persist(Entry entry) {
		entityManager.persist(entry);
	}

	public Entry merge(Entry entry) {
		return entityManager.merge(entry);
	}

	public void delete(Entry entry) {
		entityManager.remove(entry);
	}

	public int deleteAllEntries() {
		int count = entityManager.createQuery("DELETE FROM Entry").executeUpdate();
		return count;
	}

	public void delete(int id) {
		Entry entry = getEntry(id);
		if (entry != null) {
			delete(entry);
		}
	}
}