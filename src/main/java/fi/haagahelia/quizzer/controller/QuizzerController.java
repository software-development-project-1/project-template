package fi.haagahelia.quizzer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;

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
}
