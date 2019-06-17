package at.fh.swenga.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import at.fh.swenga.dao.CategoryDao;
import at.fh.swenga.model.Category;
import at.fh.swenga.model.Entry;

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
