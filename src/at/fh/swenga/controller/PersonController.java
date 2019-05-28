package at.fh.swenga.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import at.fh.swenga.dao.PersonDao;
import at.fh.swenga.model.Person;

@Controller
public class PersonController {
	@Autowired
	PersonDao personDao;

	@RequestMapping(value = { "/", "list" })
	public String index(Model model) {
		List<Person> persons = personDao.getPersons();

		model.addAttribute("persons", persons);
		return "index";
	}

	/**
	 * generates test data
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/fillData")
	@Transactional
	public String fillData(Model model) {
		Date now = new Date();

		Person p1 = new Person("Johann", "Blauensteiner", now);
		personDao.persist(p1);

		Person p2 = new Person("Max", "Mustermann", now);
		personDao.persist(p2);

		Person p3 = new Person("Jane", "Doe", now);
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

	@ExceptionHandler()
	@ResponseStatus(code = HttpStatus.FORBIDDEN)
	public String handle403(Exception ex) {
		return "login";
	}
}