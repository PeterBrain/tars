package at.fh.swenga.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import at.fh.swenga.dao.UserDao;

@Controller
public class SecurityController {

	@Autowired
	UserDao userDao;

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
