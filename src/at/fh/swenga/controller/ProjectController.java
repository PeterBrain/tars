package at.fh.swenga.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import at.fh.swenga.dao.ProjectDao;
import at.fh.swenga.dao.UserDao;
import at.fh.swenga.model.Project;
import at.fh.swenga.model.User;

@Controller
public class ProjectController {

	@Autowired
	ProjectDao projectDao;

	@Autowired
	UserDao userDao;

	@RequestMapping(value = { "listProjects" })
	public String listProjects(Model model) {
		List<Project> projects = projectDao.getProjects();
		model.addAttribute("projects", projects);

		return "listProjects";
	}

	@RequestMapping(value = { "fillProjects" })
	@Transactional
	public String fillProjects(Model model) {

		Project project1 = new Project("Project ", userDao.getUserById(0));
		projectDao.persist(project1);

		return "forward:listProjects";
	}
	
	@RequestMapping(value = { "/deleteProject" }, method = RequestMethod.GET)
	public String deleteProject(Model model, @RequestParam int id) {
		projectDao.delete(id);

		model.addAttribute("message", "Project deleted");

		return "forward:listProjects";
	}
}
