package at.fh.swenga.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.fluttercode.datafactory.impl.DataFactory;
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
	 * fill database with default users
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/fillUsers" })
	@Transactional
	public String fillData(Model model) {

		UserRole adminRole = userRoleDao.getRole("ROLE_ADMIN");
		if (adminRole == null) {
			adminRole = new UserRole("ROLE_ADMIN");
			userRoleDao.persist(adminRole);
		}

		UserRole userRole = userRoleDao.getRole("ROLE_USER");
		if (userRole == null) {
			userRole = new UserRole("ROLE_USER");
			userRoleDao.persist(userRole);
		}

		UserRole projectLeaderRole = userRoleDao.getRole("ROLE_PROJECT_LEADER");
		if (projectLeaderRole == null) {
			projectLeaderRole = new UserRole("ROLE_PROJECT_LEADER");
			userRoleDao.persist(projectLeaderRole);
		}

		DataFactory df = new DataFactory();

		// produces a date between 1/1/2000 and the current date
		Date minDate = df.getDate(2020, 1, 1);
		Date now = new Date();

		User admin = new User("Hans", "Maier", now, "admin@example.com", "admin", "password", true);
		admin.encryptPassword();
		admin.addUserRole(userRole);
		admin.addUserRole(projectLeaderRole);
		admin.addUserRole(adminRole);
		userDao.persist(admin);

		for (int i = 0; i <= 5; i++) {
			String firstname = df.getFirstName();
			User user = new User(firstname, df.getLastName(), df.getDateBetween(minDate, now),
					firstname + "@example.com", firstname.toLowerCase(), "password", true);
			user.encryptPassword();

			if (i == 0) {
				user.addUserRole(projectLeaderRole);
			}

			user.addUserRole(userRole);
			userDao.persist(user);
		}

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
		return "editUser";
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
	public String createUser(Model model, @RequestParam String firstName, @RequestParam String lastName,
			@RequestParam String email, @RequestParam String userName, @RequestParam String dateOfBirth,
			@RequestParam String password, @RequestParam String password_repeat) {

		System.out.println(firstName);

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
			return "editUser";
		}

		if (password.equals(password_repeat)) {
			User new_user = new User(firstName, lastName, dob, email, userName, password, true);
			new_user.encryptPassword();
			new_user.addUserRole(userRole);
			userDao.persist(new_user);

			List<User> users = userDao.getUsers();
			model.addAttribute("users", users);

			model.addAttribute("message", "Created new user");
			return "userManagement";
		} else {
			model.addAttribute("errorMessage", "Passwords do not match");
			return "editUser";
		}
	}

	/**
	 * 
	 * open editUser page
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = { "/editUser" }, method = RequestMethod.GET)
	public String editUser(Model model, int id) {

		User user = userDao.getUserById(id);

		if (user != null) {
			model.addAttribute("user", user);
			return "editUser";
		} else {
			model.addAttribute("errorMessage", "Couldn't find user with id:  " + id);
			return "forward:/listUsers";
		}
	}

	/**
	 * edit specific user details
	 * 
	 * @param changedUser
	 * @param bindingResult
	 * @param model
	 * @param password_repeat
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = { "/changeUser" }, method = RequestMethod.POST)
	public String changeUser(@Valid User changedUser, BindingResult bindingResult, Model model,
			@RequestParam String password_repeat) {
		// Any errors? -> Create a String out of all errors and return to the page
		if (bindingResult.hasErrors()) {
			String errorMessage = "";
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				errorMessage += fieldError.getField() + " is invalid: " + fieldError.getCode() + "<br>";
			}
			model.addAttribute("errorMessage", errorMessage);

			return "userManagement";
		}

		// Get the user we want to change
		User user = userDao.getUserById(changedUser.getUserId());

		if (user == null) {
			model.addAttribute("errorMessage", "User does not exist!<br>");
		} else {
			// Change the attributes
			user.setFirstName(changedUser.getFirstName());
			user.setLastName(changedUser.getLastName());
			user.setDateOfBirth(changedUser.getDateOfBirth());
			user.setEmail(changedUser.getEmail());
			user.setUserName(changedUser.getUserName());

			if (!changedUser.getPassword().isEmpty()) {
				if (changedUser.getPassword().equals(password_repeat)) {
					user.setPassword(password_repeat);
					user.encryptPassword();
				}
			}

			userDao.merge(user);

			// Save a message for the web page
			model.addAttribute("message",
					"Changed user " + changedUser.getFirstName() + " " + changedUser.getLastName());

		}

		List<User> users = userDao.getUsers();
		model.addAttribute("users", users);

		return "userManagement";
	}

	/**
	 * 
	 * open deleteUser page
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
		model.addAttribute("message", "User deleted");

		return "userManagement";
	}

	/**
	 * open editPassword page
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/editPassword" }, method = RequestMethod.GET)
	public String editPassword(Model model) {
		String username = userDao.getCurrentUser();
		User user = userDao.getUserByUserName(username);

		model.addAttribute("user", user);

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
	@RequestMapping(value = { "/changePassword" })
	@Transactional
	public String changePassword(Model model, @RequestParam int userId, @RequestParam String password_old,
			@RequestParam String password_new, @RequestParam String password_repeat) {
		User user = userDao.getUserById(userId);

		if (user == null) {
			System.out.println("User does not exist");
			model.addAttribute("errorMessage", "User does not exist!<br>");
		} else {
			if (!password_old.isEmpty()) {
				if (user.checkIfValidOldPassword(user, password_old)) {
					System.out.println("Current password is correct!");

					if (password_new.equals(password_repeat)) {
						System.out.println("Old password equals new password");

						user.setPassword(password_repeat);
						user.encryptPassword();

						userDao.persist(user);
//						userDao.merge(user);

						model.addAttribute("message", "New password was set!");
					} else {
						System.out.println("Passwords do not match");
						model.addAttribute("errorMessage", "Passwords do not match!");
					}
				} else {
					System.out.println("Current password is not correct");
					model.addAttribute("errorMessage", "Current password is not correct!");
				}
			} else {
				System.out.println("Current password is empty");
				model.addAttribute("errorMessage", "Current password is empty!");
			}
		}

		model.addAttribute("user", user);
		return "editPassword";
	}

	/*
	 * @ExceptionHandler()
	 * 
	 * @ResponseStatus(code = HttpStatus.FORBIDDEN) public String
	 * handle403(Exception ex) { return "login"; }
	 */

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
