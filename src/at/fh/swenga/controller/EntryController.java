package at.fh.swenga.controller;

import java.util.Date;
import java.util.List;

import org.fluttercode.datafactory.impl.DataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import at.fh.swenga.dao.EntryDao;
import at.fh.swenga.dao.UserDao;
import at.fh.swenga.model.Entry;

@Controller
public class EntryController {

	@Autowired
	EntryDao entryDao;

	@Autowired
	UserDao userDao;

	@RequestMapping(value = { "listEntries" })
	public String listEntries(Model model) {
		List<Entry> entries = entryDao.getEntries();
		model.addAttribute("entries", entries);

		String username = userDao.getCurrentUser();
		model.addAttribute("user", username);

		return "listEntries";
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

		DataFactory df = new DataFactory();

		// produces a date between 1/1/2020 and the current date
		Date minDate = df.getDate(2020, 1, 1);
		Date now = new Date();

		for (int i = 0; i <= 28; i++) {
			Entry p1 = new Entry(entryDao.getEntries().size() + 1, now, "My note: " + df.getRandomWord(),
					"My activity" + df.getRandomWord(), now, df.getDateBetween(minDate, now), true);
			p1.setEditor(userDao.getUserById(1));
			entryDao.persist(p1);
		}

//		Entry p1 = new Entry(entryDao.getEntries().size() + 1, now, "Test1", "Work1", now, now, true);
//		p1.setEditor(userDao.getUserById(1));
//		entryDao.persist(p1);
//
//		Entry p2 = new Entry(entryDao.getEntries().size() + 1, now, "Test2", "Work2", now, now, true);
//		p2.setEditor(userDao.getUserById(1));
//		entryDao.persist(p2);
//
//		Entry p3 = new Entry(entryDao.getEntries().size() + 1, now, "Test3", "Work3", now, now, true);
//		p3.setEditor(userDao.getUserById(1));
//		entryDao.persist(p3);

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