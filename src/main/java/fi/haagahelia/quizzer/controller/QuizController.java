package fi.haagahelia.quizzer.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.haagahelia.quizzer.model.Category;
import fi.haagahelia.quizzer.model.Quiz;
import fi.haagahelia.quizzer.repository.CategoryRepository;
import fi.haagahelia.quizzer.repository.QuizRepository;

@Controller
public class QuizController {
	private static final Logger logger = LoggerFactory.getLogger(QuizController.class);
	@Autowired
	private QuizRepository qrepository;
	@Autowired
	private CategoryRepository categoryrepository;

	@GetMapping("/")
	public String listQuizzes(Model model) {
		List<Quiz> quizzes = qrepository.findAll();
		model.addAttribute("quizzes", quizzes);
		return "quizzesList";
	}

	// Add new quiz:
	@GetMapping("/addQuiz")
	public String renderAddQuizForm(Model model) {
		model.addAttribute("quiz", new Quiz());
		List<Category> categories = categoryrepository.findAll();
		Collections.sort(categories, (c1, c2) -> c1.getName().compareTo(c2.getName()));
		model.addAttribute("categories", categories);

		return "addQuiz";
	}

	// Save quize
	@PostMapping("/saveQuiz")
	public String saveQuiz(@Valid @ModelAttribute("quiz") Quiz quiz, BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("quiz", quiz);
			List<Category> categories = categoryrepository.findAll();
			Collections.sort(categories, (c1, c2) -> c1.getName().compareTo(c2.getName()));
			model.addAttribute("categories", categories);
			return "addQuiz";
		}
		qrepository.save(quiz);

		return "redirect:/";
	}

	// Edit quiz by id:
	@RequestMapping("/editQuiz/{id}")
	public String editQuiz(@PathVariable("id") Long id, Model model) {
		Optional<Quiz> quizOptional = qrepository.findById(id);
		Quiz quiz = quizOptional.orElse(new Quiz());
		model.addAttribute("quiz", quiz);

		List<Category> categories = categoryrepository.findAll();
		Collections.sort(categories, (c1, c2) -> c1.getName().compareTo(c2.getName()));
		model.addAttribute("categories", categories);
		return "editQuiz";
	}

	// update quiz
	@PostMapping("/updateQuiz")
	public String updateQuiz(@Valid @ModelAttribute("quiz") Quiz updatedQuiz, BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("quiz", updatedQuiz);
			return "editQuiz";
		}

		Optional<Quiz> existingQuizOptional = qrepository.findById(updatedQuiz.getId());
		if (!existingQuizOptional.isPresent()) {
			model.addAttribute("errorMessage", "Quiz not found");
			return "error";
		}
		Quiz existingQuiz = existingQuizOptional.get();

		existingQuiz.setQuizName(updatedQuiz.getQuizName());
		existingQuiz.setQuizDescription(updatedQuiz.getQuizDescription());
		existingQuiz.setPublished(updatedQuiz.getPublished());
		existingQuiz.setCategory(updatedQuiz.getCategory());

		qrepository.save(existingQuiz);

		return "redirect:/";
	}

	// Delete quiz by id:
	@RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
	public String deleteQuiz(@PathVariable("id") Long id, Model model) {
		qrepository.deleteById(id);
		return "redirect:/";
	}
	@GetMapping("/quiz/newest")
	public String listNewestQuizzes(Model model) {
		List<Quiz> quizzes = qrepository.findAllByOrderByCreatedAtDesc();
		model.addAttribute("quizzes", quizzes);
		return "quizzesList";
	}

	@GetMapping("/quiz/oldest")
	public String listOldestQuizzes(Model model) {
		List<Quiz> quizzes = qrepository.findAllByOrderByCreatedAtAsc();
		model.addAttribute("quizzes", quizzes);
		return "quizzesList";
	}

	@GetMapping("/quiz/published")
	public String getPublishedQuizzes(Model model) {
		List<Quiz> quizzes = qrepository.findByPublished(true);
		model.addAttribute("quizzes", quizzes);
		return "quizzesList";
	}

	@GetMapping("/quiz/unpublished")
	public String getUnpublishedQuizzes(Model model) {
		List<Quiz> quizzes = qrepository.findByPublished(false);
		model.addAttribute("quizzes", quizzes);
		return "quizzesList";
	}

	// Add new category:
	@GetMapping("/addCategory")
	public String addCategoryForm(Model model) {
		model.addAttribute("category", new Category());
		return "addCategory";
	}

	// Save category
	@PostMapping("/saveCategory")
	public String saveCategoryToList(@Valid @ModelAttribute("category") Category category, BindingResult bindingResult,
			Model model) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("category", category);
			return "addCategory";
		}
		
		List<Category> categories = categoryrepository.findAll();
		String newCategoryName = category.getName();		
		for(Category existingCategory : categories ){
			if(existingCategory.getName().equalsIgnoreCase(newCategoryName)){
				model.addAttribute("errorMessage", "Category name has already existed!");
				model.addAttribute("category", category);
				return "addCategory";
			}
		}
		
		categoryrepository.save(category);
		return "redirect:/categoryList";
	}

	// Show list of categories
	@GetMapping(value = "/categoryList")
	public String listCategories(Model model) {
		List<Category> categories = categoryrepository.findAll();
		logger.info("Categories loaded: " + categories);

		if(categories!= null && !categories.isEmpty()){
			Collections.sort(categories, (c1, c2) -> c1.getName().compareTo(c2.getName()));
		}else{
			categories = Collections.emptyList();
		}
		model.addAttribute("categoryList", categories);
		return "categoryList";
	}

	// Edit category by id:
	@RequestMapping(value = "/editCategory/{id}", method = RequestMethod.GET)
    public String editCategory(@PathVariable("id") Long id, Model model) {
        Optional<Category> categoryOptional = categoryrepository.findById(id);
        Category category = categoryOptional.get();
        model.addAttribute("category", category);

        return "editCategory";
    }

	// Save updated category
	@PostMapping("/saveUpdatedCategory")
	public String saveUpdatedCategory(@Valid @ModelAttribute("category") Category category, BindingResult bindingResult,
			Model model) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("category", category);
			return "editCategory";
		}

		List<Category> categories = categoryrepository.findAll();
		String updatedCategoryName = category.getName();
		for (Category c : categories) {
			if (!c.getId().equals(category.getId()) && c.getName().equalsIgnoreCase(updatedCategoryName)) {
				model.addAttribute("errorMessage", "Category name already exists");
				model.addAttribute("category", category);
				return "editCategory";
			}
		}

		categoryrepository.save(category);
		return "redirect:/categoryList";
	}

	// Delete category by id:
    @RequestMapping(value = "/deleteCategory/{id}", method = RequestMethod.GET)
    public String deleteCategory(@PathVariable("id") Long id, Model model) {
		List<Quiz> quizzes = qrepository.findAllByCategoryId(id);
		quizzes.forEach(quiz -> {
			quiz.setCategory(null);
			qrepository.save(quiz);
		});
		
        categoryrepository.deleteById(id);
        return "redirect:/categoryList";
    }
}