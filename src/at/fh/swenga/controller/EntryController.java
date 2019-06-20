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
	 * fill database with default entries
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/fillEntries" })
	@Transactional
	public String fillEntries(Model model) {

		DataFactory df = new DataFactory();

		// produces a date between 1/1/2020 and the current date
		Date minDate = df.getDate(2019, 6, 12);
		// Date maxDate = new Date();
		Date now = new Date();

		// long duration = 0;
		// Date tsStart = new Date();
		// Date tsEnd = now;

		Entry p1 = new Entry(df.getRandomWord(), df.getRandomWord(), df.getDateBetween(minDate, now), null, now, now,
				true);
		Entry p2 = new Entry(df.getRandomWord(), df.getRandomWord(), df.getDateBetween(minDate, now), null, now, now,
				true);
		Entry p3 = new Entry(df.getRandomWord(), df.getRandomWord(), df.getDateBetween(minDate, now), null, now, now,
				true);
		Entry p4 = new Entry(df.getRandomWord(), df.getRandomWord(), df.getDateBetween(minDate, now), null, now, now,
				true);
		Entry p5 = new Entry(df.getRandomWord(), df.getRandomWord(), df.getDateBetween(minDate, now), null, now, now,
				true);
		Entry p6 = new Entry(df.getRandomWord(), df.getRandomWord(), df.getDateBetween(minDate, now), null, now, now,
				true);
		Entry p7 = new Entry(df.getRandomWord(), df.getRandomWord(), df.getDateBetween(minDate, now), null, now, now,
				true);

		p1.setTimestampEnd(java.sql.Timestamp.valueOf(p1.getTimestampStart().toInstant().atZone(ZoneId.systemDefault())
				.toLocalDateTime().plusHours(df.getNumberBetween(1, 12))));
		p2.setTimestampEnd(java.sql.Timestamp.valueOf(p2.getTimestampStart().toInstant().atZone(ZoneId.systemDefault())
				.toLocalDateTime().plusHours(df.getNumberBetween(1, 12))));
		p3.setTimestampEnd(java.sql.Timestamp.valueOf(p3.getTimestampStart().toInstant().atZone(ZoneId.systemDefault())
				.toLocalDateTime().plusHours(df.getNumberBetween(1, 12))));
		p4.setTimestampEnd(java.sql.Timestamp.valueOf(p4.getTimestampStart().toInstant().atZone(ZoneId.systemDefault())
				.toLocalDateTime().plusHours(df.getNumberBetween(1, 12))));
		p5.setTimestampEnd(java.sql.Timestamp.valueOf(p5.getTimestampStart().toInstant().atZone(ZoneId.systemDefault())
				.toLocalDateTime().plusHours(df.getNumberBetween(1, 12))));
		p6.setTimestampEnd(java.sql.Timestamp.valueOf(p6.getTimestampStart().toInstant().atZone(ZoneId.systemDefault())
				.toLocalDateTime().plusHours(df.getNumberBetween(1, 12))));
		p7.setTimestampEnd(java.sql.Timestamp.valueOf(p7.getTimestampStart().toInstant().atZone(ZoneId.systemDefault())
				.toLocalDateTime().plusHours(df.getNumberBetween(1, 12))));

//		for (int i = 1; i < 5; i++) {
//			tsStart = df.getDateBetween(minDate, now);
//
//			Entry p1 = new Entry("My note: " + df.getRandomWord(), "My activity: " + df.getRandomWord(), tsStart, now,
//					now, now, true);
//			p1.setEditor(userDao.getUserById(i));
//			p1.setProject(projectDao.getProjectById(1));
//			p1.setCategory(categoryDao.getCategoryById(1));
//
//			duration = java.time.Duration.between(tsStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
//					tsEnd.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()).toMinutes();
//
//			p1.setMinutes(duration);
//			entryDao.persist(p1);
//		}
		p1.setMinutes(
				java.time.Duration
						.between(p1.getTimestampStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
								p1.getTimestampEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
						.toMinutes());
		p2.setMinutes(
				java.time.Duration
						.between(p2.getTimestampStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
								p2.getTimestampEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
						.toMinutes());
		p3.setMinutes(
				java.time.Duration
						.between(p3.getTimestampStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
								p3.getTimestampEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
						.toMinutes());
		p4.setMinutes(
				java.time.Duration
						.between(p4.getTimestampStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
								p4.getTimestampEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
						.toMinutes());
		p5.setMinutes(
				java.time.Duration
						.between(p5.getTimestampStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
								p5.getTimestampEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
						.toMinutes());
		p6.setMinutes(
				java.time.Duration
						.between(p6.getTimestampStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
								p6.getTimestampEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
						.toMinutes());
		p7.setMinutes(
				java.time.Duration
						.between(p7.getTimestampStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
								p7.getTimestampEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
						.toMinutes());

		p1.setEditor(userDao.getUserById(2));
		p2.setEditor(userDao.getUserById(1));
		p3.setEditor(userDao.getUserById(2));
		p4.setEditor(userDao.getUserById(3));
		p5.setEditor(userDao.getUserById(4));
		p6.setEditor(userDao.getUserById(1));
		p7.setEditor(userDao.getUserById(2));

		p1.setProject(projectDao.getProjectById(1));
		p2.setProject(projectDao.getProjectById(2));
		p3.setProject(projectDao.getProjectById(3));
		p4.setProject(projectDao.getProjectById(4));
		p5.setProject(projectDao.getProjectById(1));
		p6.setProject(projectDao.getProjectById(2));
		p7.setProject(projectDao.getProjectById(3));

		p1.setCategory(categoryDao.getCategoryById(1));
		p2.setCategory(categoryDao.getCategoryById(2));
		p3.setCategory(categoryDao.getCategoryById(3));
		p4.setCategory(categoryDao.getCategoryById(4));
		p5.setCategory(categoryDao.getCategoryById(1));
		p6.setCategory(categoryDao.getCategoryById(2));
		p7.setCategory(categoryDao.getCategoryById(3));

		entryDao.persist(p1);
		entryDao.persist(p2);
		entryDao.persist(p3);
		entryDao.persist(p4);
		entryDao.persist(p5);
		entryDao.persist(p6);
		entryDao.persist(p7);

		return "forward:login";
	}

	/**
	 * open entry list
	 * 
	 * @param model
	 * @return
	 */
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

	/*
	 * @Secured("ROLE_ADMIN")
	 * 
	 * @RequestMapping(value = { "/delete" }) public String deleteData(Model
	 * model, @RequestParam int id) { entryDao.delete(id); return "forward:list"; }
	 */

	/**
	 * delete entry
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = { "/deleteEntry" }, method = RequestMethod.GET)
	public String deleteEntry(Model model, @RequestParam int id) {
		entryDao.delete(id);

		return "forward:listEntries";
	}

	/**
	 * open add entry form
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/addEntry" }, method = RequestMethod.GET)
	public String addEntry(Model model) {

		List<Project> projects = projectDao.getProjects();
		model.addAttribute("projects", projects);

		List<Category> categories = categoryDao.getCategories();
		model.addAttribute("categories", categories);

		return "editEntry";
	}

	/**
	 * create new entry
	 * 
	 * @param model
	 * @param note
	 * @param activity
	 * @param timestampStart
	 * @param timestampEnd
	 * @param new_project
	 * @param new_category
	 * @return
	 */
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

	/**
	 * open edit entry form
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
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

	/**
	 * change entry
	 * 
	 * @param changedEntry
	 * @param bindingResult
	 * @param model
	 * @param new_project
	 * @param new_category
	 * @return
	 */
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

			model.addAttribute("message", "Changed entry " + changedEntry.getEntryId());

			entryDao.merge(entry);
		}

		return "forward:listEntries";
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