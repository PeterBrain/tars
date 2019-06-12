package at.fh.swenga.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import at.fh.swenga.dao.UserDao;
import at.fh.swenga.dao.UserRoleDao;
import at.fh.swenga.model.User;
import at.fh.swenga.model.UserRole;

@Controller
public class UserController {

	@Autowired
	UserDao userDao;

	@Autowired
	UserRoleDao userRoleDao;

	/**
	 * 
	 * fills database with two default users
	 * 
	 * @param model
	 * @return
	 */
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

	/**
	 * 
	 * open and fill user management page
	 * 
	 * @param model
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = { "/listUsers" }, method = RequestMethod.GET)
	public String listUsers(Model model) {
		String username = userDao.getCurrentUser();
		model.addAttribute("user", username);

		List<User> users = userDao.getUsers();
		model.addAttribute("users", users);

		return "userManagement";
	}

	/**
	 * 
	 * open addUser page
	 * 
	 * @param model
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = { "/addUser" }, method = RequestMethod.GET)
	public String addUser(Model model) {
		String username = userDao.getCurrentUser();
		model.addAttribute("user", username);

		return "addUser";
	}

	/**
	 * 
	 * create a new user
	 * 
	 * @param model
	 * @param firstname
	 * @param lastname
	 * @param email
	 * @param username
	 * @param dateOfBirth
	 * @param password
	 * @param password_repeat
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = { "/createUser" }, method = RequestMethod.POST)
	public String createUser(Model model, @RequestParam String firstname, @RequestParam String lastname,
			@RequestParam String email, @RequestParam String username, @RequestParam String dateOfBirth,
			@RequestParam String password, @RequestParam String password_repeat) {
		String currentUsername = userDao.getCurrentUser();
		model.addAttribute("user", currentUsername);

		UserRole adminRole = userRoleDao.getRole("ROLE_ADMIN");
		if (adminRole == null) {
			adminRole = new UserRole("ROLE_ADMIN");
		}

		UserRole userRole = userRoleDao.getRole("ROLE_USER");
		if (userRole == null) {
			userRole = new UserRole("ROLE_USER");
		}

		// convert string to date
		Date dob = new Date();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			dob = sdf.parse(dateOfBirth);
		} catch (Exception e) {
			model.addAttribute("errorMessage", "Date of birth invalid");
			return "addUser";
		}

		if (password.equals(password_repeat)) {
			User new_user = new User(firstname, lastname, dob, email, username, password, true);
			new_user.encryptPassword();
			new_user.addUserRole(userRole);
			userDao.persist(new_user);

			List<User> users = userDao.getUsers();
			model.addAttribute("users", users);

			model.addAttribute("message", "Created new user");
			return "userManagement";
		} else {
			model.addAttribute("errorMessage", "Passwords do not match");
			return "addUser";
		}
	}

	/**
	 * 
	 * open addUser page
	 * 
	 * @param model
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = { "/deleteUser" }, method = RequestMethod.GET)
	public String deleteUser(Model model, @RequestParam int id) {
		userDao.delete(id);

		List<User> users = userDao.getUsers();
		model.addAttribute("users", users);

		return "userManagement";
	}

	/**
	 * 
	 * open editPassword page
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/editPassword" }, method = RequestMethod.GET)
	public String editPassword(Model model) {
		String username = userDao.getCurrentUser();
		model.addAttribute("user", username);
		
		List<User> users = userDao.findByUsername(username);
		model.addAttribute(users);

		return "editPassword";
	}

	/**
	 * 
	 * change users password
	 * 
	 * @param model
	 * @param id
	 * @param pass_old
	 * @param pass_new
	 * @param pass_repeat
	 * @return
	 */
	@RequestMapping("/changePassword")
	@Transactional
	public String changePassword(Model model, @RequestParam int id, @RequestParam String pass_old,
			@RequestParam String pass_new, @RequestParam String pass_repeat) {
		User user = userDao.getUserById(id);
		
		if (pass_new.equals(pass_repeat)) { // old password need to be checked too
			user.encryptPassword();
			userDao.persist(user);
			model.addAttribute("message", "Password successfully changed");
		} else {
			model.addAttribute("errorMessage", "Something went wrong");
		}
		return "editPassword";
	}
	
	/*@ExceptionHandler()
	@ResponseStatus(code = HttpStatus.FORBIDDEN)
	public String handle403(Exception ex) {
		return "login";
	}*/

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
