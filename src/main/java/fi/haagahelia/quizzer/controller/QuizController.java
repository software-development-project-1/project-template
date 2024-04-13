package fi.haagahelia.quizzer.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import fi.haagahelia.quizzer.model.Category;
import fi.haagahelia.quizzer.model.Quiz;
import fi.haagahelia.quizzer.repository.CategoryRepository;
import fi.haagahelia.quizzer.repository.QuizRepository;

@Controller
public class QuizController {
    
    @Autowired
    private QuizRepository qrepository;
    // @Autowired
    // private CategoryRepository catrepository;

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
        model.addAttribute("category", new Category());
		return "addQuiz";
	}

	// Save quize
	@PostMapping("/saveQuiz")
	public String saveQuiz(@Valid @ModelAttribute("quiz") Quiz quiz, BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("quiz", quiz);
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
		return "editQuiz";
	}

	// Update quize:
	@PostMapping("/updateQuiz")
	public String updateQuiz(@Valid @ModelAttribute("quiz") Quiz quiz, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("quiz", quiz);
			return "editQuiz";
		}
		qrepository.save(quiz);

		return "redirect:/";
	}

	// Delete quiz by id:
	@RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
	public String deleteQuiz(@PathVariable("id") Long id, Model model) {
		qrepository.deleteById(id);
		return "redirect:/";
	}
}
