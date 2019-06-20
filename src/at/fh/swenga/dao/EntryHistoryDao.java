package at.fh.swenga.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class EntryHistoryDao {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	
}
