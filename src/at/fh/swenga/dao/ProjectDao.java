package at.fh.swenga.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.dao.DataAccessException;
import org.springframework.security.access.annotation.Secured;
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

	public Project getProjectById(int i) throws DataAccessException {
		return entityManager.find(Project.class, i);
	}

	@Secured("ROLE_PROJECT_LEADER")
	public void persist(Project project) {
		entityManager.persist(project);
	}

	@Secured("ROLE_PROJECT_LEADER")
	public Project merge(Project project) {
		return entityManager.merge(project);
	}

	@Secured("ROLE_PROJECT_LEADER")
	public void delete(Project project) {
		entityManager.remove(project);
	}

	@Secured("ROLE_PROJECT_LEADER")
	public void delete(int id) {
		Project project = getProjectById(id);
		if (project != null) {
			delete(project);
		}
	}
}
