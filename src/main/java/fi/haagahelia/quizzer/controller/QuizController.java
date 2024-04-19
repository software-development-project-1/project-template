package fi.haagahelia.quizzer.controller;

import java.util.ArrayList;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.haagahelia.quizzer.model.Category;
import fi.haagahelia.quizzer.model.Question;
import fi.haagahelia.quizzer.model.Quiz;
import fi.haagahelia.quizzer.repository.CategoryRepository;
import fi.haagahelia.quizzer.repository.QuestionRepository;
import fi.haagahelia.quizzer.repository.QuizRepository;

@Controller
public class QuizController {
	private static final Logger logger = LoggerFactory.getLogger(QuizController.class);
	@Autowired
	private QuizRepository qrepository;
	@Autowired
	private QuestionRepository questionrepository;

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

}