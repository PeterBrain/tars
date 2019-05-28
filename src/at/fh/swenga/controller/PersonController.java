package at.fh.swenga.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import at.fh.swenga.dao.PersonDao;
import at.fh.swenga.model.PersonModel;

@Controller
public class PersonController {
	@Autowired
	PersonDao personDao;

	@RequestMapping(value = { "/", "list" })
	public String index(Model model) {
		List<PersonModel> persons = personDao.getPersons();

		model.addAttribute("persons", persons);
		return "index";
	}

	@RequestMapping("/fillData")
	@Transactional
	public String fillData(Model model) {
		Date now = new Date();

		PersonModel p1 = new PersonModel("Johann", "Blauensteiner", now);
		personDao.persist(p1);

		PersonModel p2 = new PersonModel("Max", "Mustermann", now);
		personDao.persist(p2);

		PersonModel p3 = new PersonModel("Jane", "Doe", now);
		personDao.persist(p3);

		return "forward:list";
	}

	@RequestMapping("/searchPersons")
	public String search(Model model, @RequestParam String searchString) {
		model.addAttribute("persons", personDao.searchPersons(searchString));
		return "index";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping("/delete")
	public String deleteData(Model model, @RequestParam int id) {
		personDao.delete(id);

		return "forward:list";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String handleLogin() {
		return "login";
	}

	@ExceptionHandler(Exception.class)
	public String handleAllException(Exception ex) {
		return "error";
	}
	/*
	 * @ExceptionHandler()
	 * 
	 * @ResponseStatus(code=HttpStatus.FORBIDDEN) public String handle403(Exception
	 * ex) {
	 * 
	 * return "login";
	 * 
	 * }
	 */
}