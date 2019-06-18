package at.fh.swenga.controller;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import at.fh.swenga.model.Entry;
import at.fh.swenga.model.Project;

@Controller
public class CategoryController {

	@Autowired
	CategoryDao categoryDao;

	@RequestMapping(value = { "listCategories" })
	public String listCategory(Model model) {
		List<Category> categories = categoryDao.getCategories();
		model.addAttribute("categories", categories);

		return "listCategories";
	}

	@Transactional
	@RequestMapping(value = { "/fillCategories" })
	public String fillCategories(Model model) {

		Category category1 = new Category("Customer");
		categoryDao.persist(category1);

		Category category2 = new Category("Important");
		categoryDao.persist(category2);

		Category category3 = new Category("Test");
		categoryDao.persist(category3);

		return "forward:listCategories";
	}
	
	@RequestMapping(value = { "/deleteCategory" }, method = RequestMethod.GET)
	public String deleteCategory(Model model, @RequestParam int id) {
		categoryDao.delete(id);

		model.addAttribute("message", "Category deleted");

		return "forward:listCategories";
	}

	@RequestMapping(value = { "/addCategory" }, method = RequestMethod.GET)
	public String addCategory(Model model) {
		return "editCategory";
	}
	
	@RequestMapping(value = { "/createCategory" }, method = RequestMethod.POST)
	public String createCategory(Model model, @RequestParam String name) {
		Category new_category = new Category(name);
		categoryDao.persist(new_category);

		model.addAttribute("message", "Created new Category");
		return "forward:listCategories";
	}
	
	//@Secured("PROJECT_LEADER")
		@RequestMapping(value = { "/editCategory" }, method = RequestMethod.GET)
		public String editCategory(Model model, int id) {

			Category category = categoryDao.getCategoryById(id);

			if (category != null) {
				model.addAttribute("category", category);
				return "editCategory";
			} else {
				model.addAttribute("errorMessage", "Couldn't find category with id: " + id);
				return "forward:/listCategories";
			}
		}
		
		/*@Secured("PROJECT_LEADER")*/
		@RequestMapping(value = { "/changeCategory" }, method = RequestMethod.POST)
		public String changeCategory(@Valid Category changedCategory, BindingResult bindingResult, Model model) {
			
			if (bindingResult.hasErrors()) {
				String errorMessage = "";
				for (FieldError fieldError : bindingResult.getFieldErrors()) {
					errorMessage += fieldError.getField() + " is invalid: " + fieldError.getCode() + "<br>";
				}
				model.addAttribute("errorMessage", errorMessage);

				return "editCategory";
			}

			Category category = categoryDao.getCategoryById(changedCategory.getCategoryId());

			if (category == null) {
				model.addAttribute("errorMessage", "Category does not exist!<br>");
			} else {
				category.setName(changedCategory.getName());

				categoryDao.merge(category);

				model.addAttribute("message", "Changed category " + changedCategory.getCategoryId());
			}

			return "forward:listCategories";
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
