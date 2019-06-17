package at.fh.swenga.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import at.fh.swenga.dao.ProjectDao;
import at.fh.swenga.model.Project;

@Controller
public class ProjectController {

	@Autowired
	ProjectDao projectDao;

	@RequestMapping(value = { "listProjects" })
	public String listProjects(Model model) {
		List<Project> projects = projectDao.getProjects();
		model.addAttribute("projects", projects);

		return "listProjects";
	}
}
