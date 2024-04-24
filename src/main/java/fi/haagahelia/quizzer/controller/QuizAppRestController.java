package fi.haagahelia.quizzer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import fi.haagahelia.quizzer.repository.CategoryRepository;
import fi.haagahelia.quizzer.model.Category;

@RestController
@RequestMapping("/api/QuizApp")
@CrossOrigin(origins = "*")
public class QuizAppRestController {
    @Autowired
    private CategoryRepository categoryRepository;


	@GetMapping("/categories")
	public @ResponseBody List<Category> getCategories() {

		return categoryRepository.findAll();

	}

}
