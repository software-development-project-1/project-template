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
	// @Autowired
	// private CategoryRepository catrepository;

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

	@RequestMapping(value = "/addquestion/{id}", method = RequestMethod.GET)
	public String addQuestion(@PathVariable("id") Long id, Model model) {
		Optional<Quiz> quizOptional = qrepository.findById(id);
		if (quizOptional.isPresent()) {
			Quiz quiz = quizOptional.get();

			Question newQuestion = new Question();
			newQuestion.setQuiz(quiz);

			model.addAttribute("newquestion", newQuestion);
			model.addAttribute("quiz", quiz);

			return "addQuestion";
		} else {

			return "error";
		}
	}

	@RequestMapping(value = "/addquestiontolist/{id}", method = RequestMethod.GET)
	public String addQuestions(@PathVariable("id") Long id, Model model) {
	
		Optional<Quiz> quizOptional = qrepository.findById(id);
		if (quizOptional.isPresent()) {
			Quiz quiz = quizOptional.get();

			Question newQuestion = new Question();
			newQuestion.setQuiz(quiz);

			model.addAttribute("newquestion", newQuestion);
			model.addAttribute("quiz", quiz);

			return "addQuestionToList";
		} else {
			System.out.println("Something went wrong");
			return "error";
		}
	}

	@RequestMapping(value = "/saveQuestion", method = RequestMethod.POST)
	public String saveQuestion(Question newQuestion) {
		questionrepository.save(newQuestion);
		// logger.info("Question SAVED {}", newQuestion);
		return "redirect:/";
	}

	@RequestMapping(value = "/questionList/{id}", method = RequestMethod.GET)
	public String questionList(@PathVariable("id") Long id, Model model) {
		Optional<Quiz> quizOptional = qrepository.findById(id);
		if (quizOptional.isPresent()) {
			Quiz quiz = quizOptional.get();
			List<Question> questionList = questionrepository.findByQuiz(quiz);

			model.addAttribute("questionList", questionList);
			model.addAttribute("quiz", quiz);

		}
		return "questionList";

	}
	@RequestMapping(value = "/saveQuestionToList", method = RequestMethod.POST)
	public String saveQuestionToList(Question newQuestion) {
		Long quizId = newQuestion.getQuiz().getId();
		questionrepository.save(newQuestion);
		return "redirect:/questionList/"+quizId;
	}

	// Delete question by id:
	@RequestMapping(value = "/deleteQuestion/{id}", method = RequestMethod.GET)
	public String deleteQuestion(@PathVariable("id") Long id, Model model) {

		// To retrieve the Quiz Id to put it into the redirect URL
		Optional<Question> questionOptional = questionrepository.findById(id);
		Question question = questionOptional.get();
		Quiz quiz = question.getQuiz();
		Long quizId = quiz.getId();

		questionrepository.deleteById(id);
		return "redirect:/questionList/" + quizId;
	}

	@RequestMapping(value = "/editQuestion/{id}", method = RequestMethod.GET)
	public String editProduct(@PathVariable("id") Long id, Model model) {
		Optional<Question> questionOptional = questionrepository.findById(id);
		Question question = questionOptional.get();
		Quiz quiz = question.getQuiz();
		model.addAttribute("quiz", quiz);
		model.addAttribute("questionToUpdate", question);
		return "editQuestion";
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

	@RequestMapping(value = "/questionList/{id}/{difficultyLevel}", method = RequestMethod.GET)
	public String questionListByDifficultyLevel(@PathVariable("id") Long id, @PathVariable("difficultyLevel") String difficultyLevel, Model model) {

		Optional<Quiz> quizOptional = qrepository.findById(id);
		ArrayList<Question> questionsByDifficultyLevel = new ArrayList<Question>();

		if (quizOptional.isPresent()) {

			Quiz quiz = quizOptional.get();
			List<Question> questionList = questionrepository.findByQuiz(quiz);

			for (Question question : questionList) {
				if (question.getDifficultyLevel().equals(difficultyLevel)) {
					questionsByDifficultyLevel.add(question);
				}
			}

			model.addAttribute("questionList", questionsByDifficultyLevel);
			model.addAttribute("quiz", quiz);

		}
		return "questionList";

	}

}