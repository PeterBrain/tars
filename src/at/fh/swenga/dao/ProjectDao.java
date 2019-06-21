package at.fh.swenga.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import at.fh.swenga.model.Project;

@Repository
@Transactional
public interface ProjectDao extends JpaRepository<Project, Integer> {

	/*@PersistenceContext
	protected EntityManager entityManager;

	public List<Project> getProjects() {
		TypedQuery<Project> typedQuery = entityManager.createQuery("SELECT p FROM Project p", Project.class);
		List<Project> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}

	public Project getProjectById(int i) throws DataAccessException {
		return entityManager.find(Project.class, i);
	}

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
	}*/
}
