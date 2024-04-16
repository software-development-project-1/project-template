package fi.haagahelia.quizzer.controller;

import java.time.Instant;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

// import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fi.haagahelia.quizzer.model.Quizz;
import fi.haagahelia.quizzer.repository.CategoryRepository;
import fi.haagahelia.quizzer.repository.DifficultyRepository;
import fi.haagahelia.quizzer.repository.QuestionRepository;
import fi.haagahelia.quizzer.repository.QuizzRepository;
import fi.haagahelia.quizzer.repository.StatusRepository;

@Controller
public class QuizzerController {

    @Autowired
    private QuizzRepository quizzRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private DifficultyRepository difficultyRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    // show all quizzes
    @RequestMapping(value = "/quizzlist")
    public String recipientList(Model model) {
        model.addAttribute("quizzlist", quizzRepository.findAll());
        return "quizzlist";
    }

    // add new quiz with creation date - Hong
    @RequestMapping(value = "/addquizz")

    public String addQuizz(Model model) {
        model.addAttribute("quiz", new Quizz());
        return "addquiz";
    }

    // filter quiz by date - Hong
    @GetMapping("/filterQuizzes")
    public String filterQuizzesByCreationDate(
            @RequestParam Instant creationTime,
            Model model) {
        List<Quizz> quizzes = quizzRepository.findQuizzesByCreationDateAfter(creationTime);
        model.addAttribute("quizzes", quizzes);
        return "quizzlist"; // Thymeleaf template name
    }

    @GetMapping(value = "/editquiz/{id}")
    public String editQuizForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("quizz", quizzRepository.findById(id));
        model.addAttribute("statuses", statusRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        return "editquizz.html";
    }

    @PostMapping(value = "/savequizz")
    public String save(Quizz quizz) {
        quizzRepository.save(quizz);
        return "redirect:/quizzlist";
    }

    // delete quizz
    @GetMapping("/deletequizz/{quizzId}")
    public String deleteQuizz(@PathVariable("quizzId") Long quizzId, Model model) {
        quizzRepository.deleteById(quizzId);
        return "redirect:../quizzlist";
    }
}