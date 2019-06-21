package at.fh.swenga.controller;

import java.util.List;
import java.util.Optional;

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

	/**
	 * fill database with default projects
	 * 
	 * @param model
	 * @return
	 */
	@Transactional
	@RequestMapping(value = { "/fillProjects" })
	public String fillProjects(Model model) {

		Project project1 = new Project("Project 1", "Default description 1", userDao.getUserById(2));
		projectDao.save(project1);

		Project project2 = new Project("Project 2", "Default description 2", userDao.getUserById(2));
		projectDao.save(project2);

		Project project3 = new Project("Project 3", "Default description 3", userDao.getUserById(2));
		projectDao.save(project3);
		
		Project project4 = new Project("Project 4", "Default description 4", userDao.getUserById(2));
		projectDao.save(project4);

		return "forward:login";
	}

	/**
	 * open project list
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/listProjects" })
	public String listProjects(Model model) {
		List<Project> projects = projectDao.findAll();
		model.addAttribute("projects", projects);

		return "listProjects";
	}

	/**
	 * delete a project
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@Secured("ROLE_PROJECT_LEADER")
	@RequestMapping(value = { "/deleteProject" }, method = RequestMethod.GET)
	public String deleteProject(Model model, @RequestParam int id) {
		projectDao.deleteById(id);

		model.addAttribute("message", "Project deleted");

		return "forward:listProjects";
	}

	/**
	 * open add project form
	 * 
	 * @param model
	 * @return
	 */
	@Secured("ROLE_PROJECT_LEADER")
	@RequestMapping(value = { "/addProject" }, method = RequestMethod.GET)
	public String addProject(Model model) {

		List<User> users = userDao.getUsersWithRole(3);
		users.remove(0);
		model.addAttribute("users", users);

		return "editProject";
	}

	/**
	 * create new project
	 * 
	 * @param model
	 * @param name
	 * @param description
	 * @param new_projectLeader
	 * @return
	 */
	@Secured("ROLE_PROJECT_LEADER")
	@RequestMapping(value = { "/createProject" }, method = RequestMethod.POST)
	public String createProject(Model model, @RequestParam String name, @RequestParam String description,
			@RequestParam String new_projectLeader) {
		Project new_project = new Project(name, description, userDao.getUserByUserName(new_projectLeader));
		projectDao.save(new_project);

		model.addAttribute("message", "Created new Project");
		return "forward:listProjects";
	}

	/**
	 * open edit project form
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@Secured("ROLE_PROJECT_LEADER")
	@RequestMapping(value = { "/editProject" }, method = RequestMethod.GET)
	public String editProject(Model model, int id) {

		List<User> users = userDao.getUsersWithRole(3);
		users.remove(0);
		model.addAttribute("users", users);

		Project project = projectDao.findById(id).get();

		if (project != null) {
			model.addAttribute("project", project);
			return "editProject";
		} else {
			model.addAttribute("errorMessage", "Couldn't find project with id: " + id);
			return "forward:listProjects";
		}
	}

	/**
	 * edit project
	 * 
	 * @param changedProject
	 * @param bindingResult
	 * @param model
	 * @param new_projectLeader
	 * @return
	 */
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

		Optional<Project> projectOpt = projectDao.findById(changedProject.getProjectId());
		User user = userDao.getUserByUserName(new_projectLeader);

		if (!projectOpt.isPresent())
			throw new IllegalArgumentException("No project with id " + changedProject.getProjectId());
		
		Project project = projectOpt.get();
		if (project == null) {
			model.addAttribute("errorMessage", "Project does not exist!<br>");
		} else {
			project.setName(changedProject.getName());
			project.setDescription(changedProject.getDescription());
			project.setProjectLeader(user);

			projectDao.save(project);

			model.addAttribute("message", "Changed project " + changedProject.getProjectId());
		}

		return "forward:listProjects";
	}

	/**
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
