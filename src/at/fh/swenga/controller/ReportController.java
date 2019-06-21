package at.fh.swenga.controller;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import at.fh.swenga.dao.EntryDao;
import at.fh.swenga.dao.EntryRepository;
import at.fh.swenga.dao.UserDao;
import at.fh.swenga.model.Entry;
import at.fh.swenga.model.User;

@Controller
public class ReportController {
	@Autowired
	EntryDao entryDao;

	@Autowired
	UserDao userDao;

	@Autowired
	private MailSender mailSender;

	@Autowired
	private SimpleMailMessage templateMessage;

	@Autowired
	EntryRepository entryRepository;

	@RequestMapping(value = { "/report" })
	public String report(Model model, @RequestParam(required = false) String excel,
			@RequestParam(required = false) String mail,
			@RequestParam(name = "entryId", required = false) List<Integer> entryIds) {

		// check if checkboxes were selected
		if (CollectionUtils.isEmpty(entryIds)) {
			model.addAttribute("errorMessage", "No entry or entries selected!");
			return "forward:listEntries";
		}

		List<Entry> entries = entryRepository.findAllById(entryIds);
		model.addAttribute("entries", entries);

		// check which submit button was pressed
		if (StringUtils.isNoneEmpty(excel)) {
			return "excelReport";
		} else if (StringUtils.isNoneEmpty(mail)) {
			sendMail(entries);
			model.addAttribute("message", "Mail sent");
			return "forward:listEntries";
		} else {
			model.addAttribute("message", "An error occurred");
			return "forward:listEntries";
		}
	}

	private void sendMail(List<Entry> entries) {

		// build content of entries for mail
		String content = "";
		for (Entry entry : entries) {
			content += entry.getNote() + " " + entry.getActivity() + " " + entry.getEditor().getUserName() + " "
					+ entry.getTimestampStart() + " " + entry.getTimestampEnd() + "\n";
		}

		// thread safe "copy" of the template message
		SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);

		String username = userDao.getCurrentUser();
		User user = userDao.getUserByUserName(username);

		// override default settings from dispatcher-servlet.xml
		// msg.setFrom(from);
		msg.setTo(user.getEmail());
		msg.setSubject("EXPORT | TARS - Time & Activity Recording Software");
		msg.setText(String.format(msg.getText(), user.getFirstName() + " " + user.getLastName(), content,
				"TARS - Time & Activity Recording Software"));
		try {
			this.mailSender.send(msg);
		} catch (MailException ex) {
			ex.printStackTrace();
		}
	}

	@ExceptionHandler(Exception.class)
	public String handleAllException(Exception ex) {
		return "error";
	}

}