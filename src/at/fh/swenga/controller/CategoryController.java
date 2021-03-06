package at.fh.swenga.controller;

import java.util.List;

import javax.validation.Valid;

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
import at.fh.swenga.model.Category;

@Controller
public class CategoryController {

	@Autowired
	CategoryDao categoryDao;

	/**
	 * fill database with default categories
	 * 
	 * @param model
	 * @return
	 */
	@Transactional
	@RequestMapping(value = { "/fillCategories" })
	public String fillCategories(Model model) {

		// test data for category select
		Category category1 = new Category("Meeting");
		categoryDao.save(category1);

		Category category2 = new Category("Planning");
		categoryDao.save(category2);

		Category category3 = new Category("Travel");
		categoryDao.save(category3);

		Category category4 = new Category("Coding");
		categoryDao.save(category4);

		return "forward:login";
	}

	/**
	 * open category list
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/listCategories" })
	public String listCategory(Model model) {

		// retrieve the list of categories and add it to the model
//		List<Category> categories = categoryDao.getCategories();
		List<Category> categories = categoryDao.findAll();
		model.addAttribute("categories", categories);

		return "listCategories";
	}

	/**
	 * delete category
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
//	@RequestMapping(value = { "/deleteCategory" }, method = RequestMethod.GET)
//	public String deleteCategory(Model model, @RequestParam int id) {
//		categoryDao.delete(id);
//
//		model.addAttribute("message", "Category deleted");
//
//		return "forward:listCategories";
//	}
//	@RequestMapping(value = { "/deleteCategory" }, method = RequestMethod.GET)
//	public String deleteCategory(Model model, @RequestParam int id) {
//		categoryDao.deleteById(id);
//
//		model.addAttribute("message", "Category deleted");
//
//		return "forward:listCategories";
//	}

	/**
	 * open add category form
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/addCategory" }, method = RequestMethod.GET)
	public String addCategory(Model model) {
		return "editCategory";
	}

	/**
	 * create new category
	 * 
	 * @param model
	 * @param name
	 * @return
	 */
	@RequestMapping(value = { "/createCategory" }, method = RequestMethod.POST)
	public String createCategory(Model model, @RequestParam String name) {
		// create a new category, save it to the database and add a message to the model
		Category new_category = new Category(name);
		categoryDao.save(new_category);

		model.addAttribute("message", "Created new Category");
		return "forward:listCategories";
	}

	/**
	 * open edit category form
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = { "/editCategory" }, method = RequestMethod.GET)
	public String editCategory(Model model, int id) {

		Category category = categoryDao.findById(id).get();

		if (category != null) {
			model.addAttribute("category", category);
			return "editCategory";
		} else {
			model.addAttribute("errorMessage", "Couldn't find category with id: " + id);
			return "forward:listCategories";
		}
	}

	/**
	 * change category
	 * 
	 * @param changedCategory
	 * @param bindingResult
	 * @param model
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = { "/changeCategory" }, method = RequestMethod.POST)
	public String changeCategory(@Valid Category changedCategory, BindingResult bindingResult, Model model) {

		// check for errors
		if (bindingResult.hasErrors()) {
			String errorMessage = "";
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				errorMessage += fieldError.getField() + " is invalid: " + fieldError.getCode() + "<br>";
			}
			model.addAttribute("errorMessage", errorMessage);

			return "editCategory";
		}

		// if no errors occured, set the category to the changed one
		Category category = categoryDao.findById(changedCategory.getCategoryId()).get();

		if (category == null) {
			model.addAttribute("errorMessage", "Category does not exist!<br>");
		} else {
			category.setName(changedCategory.getName());

			categoryDao.save(category);

			model.addAttribute("message", "Changed category " + changedCategory.getCategoryId());
		}

		return "forward:listCategories";
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
