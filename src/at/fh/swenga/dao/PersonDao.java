package at.fh.swenga.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.fh.swenga.model.Person;

@Repository
@Transactional
public class PersonDao {

	@PersistenceContext
	protected EntityManager entityManager;

	public List<Person> getPersons() {
		TypedQuery<Person> typedQuery = entityManager.createQuery("SELECT e FROM Person e", Person.class);
		List<Person> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}

	public List<Person> searchPersons(String searchString) {
		TypedQuery<Person> typedQuery = entityManager.createQuery(
				"SELECT e FROM Person e WHERE e.firstName LIKE :search or e.lastName LIKE :search", Person.class);
		typedQuery.setParameter("search", "%" + searchString + "%");
		List<Person> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}

	public Person getPerson(int i) throws DataAccessException {
		return entityManager.find(Person.class, i);
	}

	public void persist(Person person) {
		entityManager.persist(person);
	}

	public Person merge(Person person) {
		return entityManager.merge(person);
	}

	public void delete(Person person) {
		entityManager.remove(person);
	}

	public int deleteAllPersons() {
		int count = entityManager.createQuery("DELETE FROM Person").executeUpdate();
		return count;
	}

	public void delete(int id) {
		Person person = getPerson(id);
		if (person != null) {
			delete(person);
		}
	}
}