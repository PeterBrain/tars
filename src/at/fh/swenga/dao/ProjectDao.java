package at.fh.swenga.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import at.fh.swenga.model.Project;

@Repository
@Transactional
public class ProjectDao {

	@PersistenceContext
	protected EntityManager entityManager;

	public List<Project> getProjects() {
		TypedQuery<Project> typedQuery = entityManager.createQuery("SELECT p FROM Project p", Project.class);
		List<Project> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}
}
