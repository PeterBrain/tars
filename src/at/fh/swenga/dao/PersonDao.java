package at.fh.swenga.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.fh.swenga.model.PersonModel;

@Repository
@Transactional
public class PersonDao {

	@PersistenceContext
	protected EntityManager entityManager;

	public List<PersonModel> getPersons() {
		TypedQuery<PersonModel> typedQuery = entityManager.createQuery("SELECT e FROM PersonModel e",
				PersonModel.class);
		List<PersonModel> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}

	public List<PersonModel> searchPersons(String searchString) {
		TypedQuery<PersonModel> typedQuery = entityManager.createQuery(
				"SELECT e FROM PersonModel e WHERE e.firstName LIKE :search or e.lastName LIKE :search",
				PersonModel.class);
		typedQuery.setParameter("search", "%" + searchString + "%");
		List<PersonModel> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}

	public PersonModel getPerson(int i) throws DataAccessException {
		return entityManager.find(PersonModel.class, i);
	}

	public void persist(PersonModel person) {
		entityManager.persist(person);
	}

	public PersonModel merge(PersonModel person) {
		return entityManager.merge(person);
	}

	public void delete(PersonModel person) {
		entityManager.remove(person);
	}

	public int deleteAllPersons() {
		int count = entityManager.createQuery("DELETE FROM PersonModel").executeUpdate();
		return count;
	}

	public void delete(int id) {
		PersonModel person = getPerson(id);
		if (person != null) {
			delete(person);
		}
	}
}