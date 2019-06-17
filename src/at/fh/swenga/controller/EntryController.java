package at.fh.swenga.controller;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.fluttercode.datafactory.impl.DataFactory;
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

		for (int i = 0; i < 28; i++) {
			Entry p1 = new Entry("My note: " + df.getRandomWord(), "My activity: " + df.getRandomWord(), now,
					df.getDateBetween(minDate, now), true);
			p1.setEditor(userDao.getUserById(i));
			entryDao.persist(p1);
		}

		return "forward:listEntries";
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

	@RequestMapping(value = { "/addEntry" }, method = RequestMethod.GET)
	public String addEntry(Model model) {
		return "editEntry";
	}

	@RequestMapping(value = { "/createEntry" }, method = RequestMethod.POST)
	public String createEntry(Model model, @RequestParam String note, @RequestParam String activity) {
		// User currentUser = userDao.get

		Date now = new Date();

		Entry new_entry = new Entry(note, activity, now, now, true);

		entryDao.persist(new_entry);

		List<Entry> entries = entryDao.getEntries();
		model.addAttribute("entries", entries);

		model.addAttribute("message", "Created new Entry");
		return "listEntries";
	}

	@RequestMapping(value = { "/editEntry" }, method = RequestMethod.GET)
	public String editEntry(Model model, int id) {
		Entry entry = entryDao.getEntryById(id);

		if (entry != null) {
			model.addAttribute("entry", entry);
			return "editEntry";
		} else {
			model.addAttribute("errorMessage", "Couldn't find entry with id: " + id);
			return "forward:/listEntries";
		}
	}

	@RequestMapping(value = { "/changeEntry" }, method = RequestMethod.POST)
	public String changeEntry(@Valid Entry changedEntry, BindingResult bindingResult, Model model) {

		// any errors? create string of all errors and return to page
		if (bindingResult.hasErrors()) {
			String errorMessage = "";
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				errorMessage += fieldError.getField() + " is invalid: " + fieldError.getCode() + "<br>";
			}
			model.addAttribute("errorMessage", errorMessage);
			return "listEntries";
		}

		Entry entry = entryDao.getEntryById(changedEntry.getEntryId());

		if (entry == null) {
			model.addAttribute("errorMessage", "Entry does not exist! <br>");
		} else {

			Date now = new Date();

			entry.setActivity(changedEntry.getActivity());
			entry.setNote(changedEntry.getActivity());
			entry.setTimestampCreated(changedEntry.getTimestampCreated());
			entry.setTimestampModified(now);

			model.addAttribute("message", "Changed entry " + changedEntry.getActivity());

		}
		return "listEntries";
	}

	@RequestMapping(value = { "/deleteEntry" }, method = RequestMethod.GET)
	public String deleteEntry(Model model, @RequestParam int id) {
		entryDao.delete(id);

		return "listEntries";
	}

}