package at.fh.swenga.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

	@Transactional
	@RequestMapping(value = { "fillProjects" })
	public String fillProjects(Model model) {

		Project project1 = new Project("Project ", "Default description", userDao.getUserById(1));
		projectDao.persist(project1);

		return "forward:listProjects";
	}

	@RequestMapping(value = { "listProjects" })
	public String listProjects(Model model) {
		List<Project> projects = projectDao.getProjects();
		model.addAttribute("projects", projects);

		return "listProjects";
	}

	@RequestMapping(value = { "/deleteProject" }, method = RequestMethod.GET)
	public String deleteProject(Model model, @RequestParam int id) {
		projectDao.delete(id);

		model.addAttribute("message", "Project deleted");

		return "forward:listProjects";
	}

	@RequestMapping(value = { "/addProject" }, method = RequestMethod.GET)
	public String addProject(Model model) {

		List<User> users = userDao.getUsers();
		model.addAttribute("users", users);

		return "editProject";
	}

	@RequestMapping(value = { "/createProject" }, method = RequestMethod.POST)
	public String createProject(Model model, @RequestParam String name, @RequestParam String description,
			@RequestParam String new_projectLeader) {
		Project new_project = new Project(name, description, userDao.getUserByUserName(new_projectLeader));
		projectDao.persist(new_project);

		model.addAttribute("message", "Created new Project");
		return "forward:listProjects";
	}

	@Secured("ROLE_PROJECT_LEADER")
	@RequestMapping(value = { "/editProject" }, method = RequestMethod.GET)
	public String editProject(Model model, int id) {

		List<User> users = userDao.getUsers();
		model.addAttribute("users", users);

		Project project = projectDao.getProjectById(id);

		if (project != null) {
			model.addAttribute("project", project);
			return "editProject";
		} else {
			model.addAttribute("errorMessage", "Couldn't find project with id: " + id);
			return "forward:/listProjects";
		}
	}

	@Secured("ROLE_PROJECT_LEADER")
	@RequestMapping(value = { "/changeProject" }, method = RequestMethod.POST)
	public String changeProject(@Valid Project changedProject, BindingResult bindingResult, Model model,
			@RequestParam String new_projectLeader) {

		if (bindingResult.hasErrors()) {
			String errorMessage = "";
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				errorMessage += fieldError.getField() + " is invalid: " + fieldError.getCode() + "<br>";
			}
			model.addAttribute("errorMessage", errorMessage);

			return "editProject";
		}

		Project project = projectDao.getProjectById(changedProject.getProjectId());
		User user = userDao.getUserByUserName(new_projectLeader);

		if (project == null) {
			model.addAttribute("errorMessage", "Project does not exist!<br>");
		} else {
			project.setName(changedProject.getName());
			project.setDescription(changedProject.getDescription());
			project.setProjectLeader(user);

			projectDao.merge(project);

			model.addAttribute("message", "Changed project " + changedProject.getProjectId());
		}

		return "forward:listProjects";
	}

	/**
	 * 
	 * handle errors
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public String handleAllException(Exception ex) {
		return "error";
	}
}
