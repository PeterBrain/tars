package at.fh.swenga.controller;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import at.fh.swenga.dao.UserDao;
import at.fh.swenga.dao.UserRoleDao;
import at.fh.swenga.model.User;
import at.fh.swenga.model.UserRole;

@Controller
public class SecurityController {

	@Autowired
	UserDao userDao;

	@Autowired
	UserRoleDao userRoleDao;

	@RequestMapping(value = { "/", "dashboard" })
	public String index(Model model) {
		String username = userDao.getCurrentUser();
		model.addAttribute("user", username);
		
		return "index";
	}
	
	@RequestMapping(value = { "/login" }, method = RequestMethod.GET)
	public String handleLogin() {
		return "login";
	}
	
	@RequestMapping(value = { "/editPassword" }, method = RequestMethod.GET)
	public String editPassword() {
		return "editPassword";
	}

	@RequestMapping("/fillUsers")
	@Transactional
	public String fillData(Model model) {

		UserRole adminRole = userRoleDao.getRole("ROLE_ADMIN");
		if (adminRole == null) {
			adminRole = new UserRole("ROLE_ADMIN");
		}

		UserRole userRole = userRoleDao.getRole("ROLE_USER");
		if (userRole == null) {
			userRole = new UserRole("ROLE_USER");
		}

		Date now = new Date();

		User admin = new User("Hans", "Maier", now, "admin@example.com", "admin", "password", true);
		admin.encryptPassword();
		admin.addUserRole(userRole);
		admin.addUserRole(adminRole);
		userDao.persist(admin);

		User user = new User("Max", "Mustermann", now, "user@example.com", "user", "password", true);
		user.encryptPassword();
		user.addUserRole(userRole);
		userDao.persist(user);

		return "forward:login";
	}

	@ExceptionHandler(Exception.class)
	public String handleAllException(Exception ex) {
		return "error";
	}
}
