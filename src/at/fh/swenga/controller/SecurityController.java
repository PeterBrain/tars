package at.fh.swenga.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import at.fh.swenga.dao.SecurityMessageDao;
import at.fh.swenga.dao.UserDao;
import at.fh.swenga.model.SecurityMessage;

@Controller
public class SecurityController {

	@Autowired
	UserDao userDao;

	@Autowired
	SecurityMessageDao securityMessageDao;

	/**
	 * open dashboard or startpage
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/", "/dashboard" })
	public String index(Model model) {
		String username = userDao.getCurrentUser();
		model.addAttribute("user", username);

		return "index";
	}

	/**
	 * open login page
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/login" }, method = RequestMethod.GET)
	public String handleLogin(Model model) {
		if (userDao.getCurrentUser().toUpperCase().equals("ANONYMOUSUSER")) {
			return "login";
		} else {
			return "index";
		}
	}

	@RequestMapping(value = { "/listSecurityMessages" })
	public String listSecurityMessages(Model model) {
		List<SecurityMessage> securityMessages = securityMessageDao.getSecurityMessages();
		model.addAttribute("securityMessages", securityMessages);

		return "listSecurityMessages";
	}

	@RequestMapping(value = { "/deleteSecurityMessage" }, method = RequestMethod.GET)
	public String deleteSecurityMessage(Model model, @RequestParam int id) {
		securityMessageDao.delete(id);

		model.addAttribute("message", "Message deleted");

		return "forward:listSecurityMessages";
	}

	@RequestMapping(value = { "/addSecurityMessage" }, method = RequestMethod.GET)
	public String addSecurityMessage(Model model) {
		return "editSecurityMessage";
	}

	@RequestMapping(value = { "/createSecurityMessage" }, method = RequestMethod.POST)
	public String createSecurityMessage(Model model, @RequestParam String title, @RequestParam String message) {
		SecurityMessage new_securityMessage = new SecurityMessage(title, message);
		securityMessageDao.persist(new_securityMessage);

		model.addAttribute("message", "Created new Message");
		return "forward:listSecurityMessages";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = { "/editSecurityMessage" }, method = RequestMethod.GET)
	public String editSecurityMessage(Model model, int id) {

		SecurityMessage securityMessage = securityMessageDao.getSecurityMessageById(id);

		if (securityMessage != null) {
			model.addAttribute("securityMessage", securityMessage);
			return "editSecurityMessage";
		} else {
			model.addAttribute("errorMessage", "Couldn't find security message with id: " + id);
			return "forward:listSecurityMessage";
		}
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = { "/changeSecurityMessage" }, method = RequestMethod.POST)
	public String changeSecurityMessage(@Valid SecurityMessage changedSecurityMessage, BindingResult bindingResult,
			Model model) {

		if (bindingResult.hasErrors()) {
			String errorMessage = "";
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				errorMessage += fieldError.getField() + " is invalid: " + fieldError.getCode() + "<br>";
			}
			model.addAttribute("errorMessage", errorMessage);

			return "editSecurityMessage";
		}

		SecurityMessage securityMessage = securityMessageDao
				.getSecurityMessageById(changedSecurityMessage.getSecurityMessageId());

		if (securityMessage == null) {
			model.addAttribute("errorMessage", "Security Message does not exist!<br>");
		} else {
			securityMessage.setTitle(changedSecurityMessage.getTitle());
			securityMessage.setMessage(changedSecurityMessage.getMessage());

			securityMessageDao.merge(securityMessage);

			model.addAttribute("message", "Changed Security Message " + changedSecurityMessage.getSecurityMessageId());
		}

		return "forward:listSecurityMessages";
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
