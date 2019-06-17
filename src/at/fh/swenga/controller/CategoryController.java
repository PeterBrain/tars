package at.fh.swenga.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import at.fh.swenga.dao.CategoryDao;
import at.fh.swenga.model.Category;

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
}
