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

import at.fh.swenga.dao.EntryDao;
import at.fh.swenga.model.Entry;

@Controller
public class EntryController {
	@Autowired
	EntryDao entryDao;

	@RequestMapping(value = { "/", "list" })
	public String index(Model model) {
		List<Entry> entries = entryDao.getEntries();

		model.addAttribute("entries", entries);
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

		/*Entry p1 = new Entry("Johann", "Blauensteiner", now);
		entryDao.persist(p1);

		Entry p2 = new Entry("Max", "Mustermann", now);
		entryDao.persist(p2);

		Entry p3 = new Entry("Jane", "Doe", now);
		entryDao.persist(p3);*/

		return "forward:list";
	}

	@RequestMapping("/searchEntries")
	public String search(Model model, @RequestParam String searchString) {
		model.addAttribute("entries", entryDao.searchEntries(searchString));
		return "index";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping("/delete")
	public String deleteData(Model model, @RequestParam int id) {
		entryDao.delete(id);
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