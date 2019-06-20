package at.fh.swenga.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import at.fh.swenga.model.EntryHistory;

@Repository
@Transactional
public class EntryHistoryDao {

	@PersistenceContext
	protected EntityManager entityManager;

	public void persist(EntryHistory entryHistory) {
		entityManager.persist(entryHistory);
	}

	public EntryHistory merge(EntryHistory entryHistory) {
		return entityManager.merge(entryHistory);
	}

}
