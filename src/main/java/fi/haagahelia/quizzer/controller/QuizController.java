package fi.haagahelia.quizzer.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

	@GetMapping("/addQuiz")
	public String renderAddQuizForm(Model model) {
		model.addAttribute("quiz", new Quiz());
        model.addAttribute("category", new Category());
		return "addQuiz";
	}

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

	@GetMapping("/editQuiz/{id}")
	public String editQuiz(@PathVariable("id") Long id, Model model) {
		Optional<Quiz> quiz = qrepository.findById(id);
		if (quiz.isPresent()) {
            model.addAttribute("quiz", quiz.get());
            return "editQuiz";
		}
		return "redirect:/";
	}

}
