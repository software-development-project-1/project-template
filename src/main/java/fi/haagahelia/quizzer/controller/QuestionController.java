package fi.haagahelia.quizzer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.validation.BindingResult;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;

import fi.haagahelia.quizzer.repository.DifficultyRepository;
import fi.haagahelia.quizzer.repository.QuestionRepository;
import fi.haagahelia.quizzer.repository.QuizzRepository;

@Controller
public class QuestionController {

    @Autowired
    private QuizzRepository quizzRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private DifficultyRepository difficultyRepository;
}