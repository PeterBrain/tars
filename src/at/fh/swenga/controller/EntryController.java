package at.fh.swenga.controller;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
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

import at.fh.swenga.dao.CategoryDao;
import at.fh.swenga.dao.EntryDao;
import at.fh.swenga.dao.ProjectDao;
import at.fh.swenga.dao.UserDao;
import at.fh.swenga.model.Category;
import at.fh.swenga.model.Entry;
import at.fh.swenga.model.Project;
import at.fh.swenga.model.User;

@Controller
public class EntryController {

	@Autowired
	EntryDao entryDao;

	@Autowired
	UserDao userDao;

	@Autowired
	ProjectDao projectDao;

	@Autowired
	CategoryDao categoryDao;

	/**
	 * fill database with test data
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/fillEntries" })
	@Transactional
	public String fillEntries(Model model) {

		DataFactory df = new DataFactory();

		// produces a date between 1/1/2020 and the current date
		Date minDate = df.getDate(2019, 1, 1);
		Date now = new Date();

		long duration = 0;
		Date tsStart = new Date();
		Date tsEnd = now;

		for (int i = 1; i < 5; i++) {
			tsStart = df.getDateBetween(minDate, now);

			Entry p1 = new Entry("My note: " + df.getRandomWord(), "My activity: " + df.getRandomWord(), tsStart, now,
					now, now, true);
			p1.setEditor(userDao.getUserById(i));
			p1.setProject(projectDao.getProjectById(1));
			p1.setCategory(categoryDao.getCategoryById(1));

			duration = java.time.Duration.between(tsStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
					tsEnd.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()).toMinutes();

			p1.setMinutes(duration);
			entryDao.persist(p1);
		}

		return "forward:login";
	}

	@RequestMapping(value = { "/listEntries" })
	public String listEntries(Model model) {
		List<Entry> entries = entryDao.getEntries();

		for (Entry entry : entries) {
			float hours = (float) entry.getMinutes() / 60F;
			float hoursRounded = (float) Math.round(hours * 100) / 100;
			entry.setMinutes(hoursRounded);
		}

		model.addAttribute("entries", entries);

		return "listEntries";
	}

	@RequestMapping(value = { "/searchEntries" })
	public String search(Model model, @RequestParam String searchString) {
		model.addAttribute("entries", entryDao.searchEntries(searchString));
		return "index";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = { "/delete" })
	public String deleteData(Model model, @RequestParam int id) {
		entryDao.delete(id);
		return "forward:list";
	}

	@RequestMapping(value = { "/addEntry" }, method = RequestMethod.GET)
	public String addEntry(Model model) {

		List<Project> projects = projectDao.getProjects();
		model.addAttribute("projects", projects);

		List<Category> categories = categoryDao.getCategories();
		model.addAttribute("categories", categories);

		return "editEntry";
	}

	@RequestMapping(value = { "/createEntry" }, method = RequestMethod.POST)
	public String createEntry(Model model, @RequestParam String note, @RequestParam String activity,
			@RequestParam String timestampStart, @RequestParam String timestampEnd, @RequestParam int new_project,
			@RequestParam int new_category) {
		// User currentUser = userDao.get

		String currentUsername = userDao.getCurrentUser();
		User currentUser = userDao.getUserByUserName(currentUsername);

		Date now = new Date();

		long duration = 0;

		// convert string to date
		Date tsStart = new Date();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
			tsStart = sdf.parse(timestampStart);
		} catch (Exception e) {
			model.addAttribute("errorMessage", "Start date invalid");
			return "editEntry";
		}
		Date tsEnd = null;
		// if Start AND End time are filled in:
		if (!(timestampEnd.isEmpty())) {

			// convert string to date
			tsEnd = new Date();
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
				tsEnd = sdf.parse(timestampEnd);
			} catch (Exception e) {
				model.addAttribute("errorMessage", "End Date invalid");
				return "editEntry";
			}

			if (tsEnd.before(tsStart)) {
				model.addAttribute("errorMessage", "End Date must be after Start Date");
				return "editEntry";
			} else {
				duration = java.time.Duration
						.between(tsStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
								tsEnd.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
						.toMinutes();
			}

		}
		Entry new_entry = new Entry(note, activity, tsStart, tsEnd, now, now, true);

		new_entry.setProject(projectDao.getProjectById(new_project));
		new_entry.setCategory(categoryDao.getCategoryById(new_category));
		new_entry.setMinutes(duration);
		new_entry.setEditor(currentUser);

		entryDao.persist(new_entry);

		model.addAttribute("message", "Created new Entry");
		return "forward:listEntries";
	}

	@RequestMapping(value = { "/editEntry" }, method = RequestMethod.GET)
	public String editEntry(Model model, int id) {

		List<Project> projects = projectDao.getProjects();
		model.addAttribute("projects", projects);

		List<Category> categories = categoryDao.getCategories();
		model.addAttribute("categories", categories);

		Entry entry = entryDao.getEntryById(id);

		if (entry != null) {
			model.addAttribute("entry", entry);
			return "editEntry";
		} else {
			model.addAttribute("errorMessage", "Couldn't find entry with id: " + id);
			return "forward:listEntries";
		}
	}

	@RequestMapping(value = { "/changeEntry" }, method = RequestMethod.POST)
	public String changeEntry(@Valid Entry changedEntry, BindingResult bindingResult, Model model,
			@RequestParam int new_project, @RequestParam int new_category) {

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
		Project project = projectDao.getProjectById(new_project);
		Category category = categoryDao.getCategoryById(new_category);

		if (entry == null) {
			model.addAttribute("errorMessage", "Entry does not exist! <br>");
		} else {

			long duration = 0;

			Date now = new Date();
			Date tsStart = changedEntry.getTimestampStart();
			Date tsEnd = changedEntry.getTimestampEnd();

			entry.setActivity(changedEntry.getActivity());
			entry.setNote(changedEntry.getNote());
			entry.setTimestampCreated(changedEntry.getTimestampCreated());
			entry.setTimestampModified(now);
			entry.setTimestampStart(changedEntry.getTimestampStart());
			entry.setTimestampEnd(changedEntry.getTimestampEnd());
			entry.setProject(project);
			entry.setCategory(category);

			if (!(tsEnd == null)) {

				if (tsEnd.before(tsStart)) {
					model.addAttribute("errorMessage", "End Date must be after Start Date");
					model.addAttribute("entry", entry);
					List<Project> projects = projectDao.getProjects();
					model.addAttribute("projects", projects);
					List<Category> categories = categoryDao.getCategories();
					model.addAttribute("categories", categories);
					return "editEntry";
				} else {
					duration = java.time.Duration
							.between(tsStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
									tsEnd.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
							.toMinutes();
				}

				entry.setMinutes(duration);
			}

			model.addAttribute("message", "Changed entry " + changedEntry.getActivity());

			entryDao.merge(entry);
		}

//		model.addAttribute("entry", entry);

		return "forward:listEntries";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = { "/deleteEntry" }, method = RequestMethod.GET)
	public String deleteEntry(Model model, @RequestParam int id) {
		entryDao.delete(id);

		return "forward:listEntries";
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