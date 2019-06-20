package at.fh.swenga.dao;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class SecurityMessageDao {
	
	protected EntityManager entityManager;
}
