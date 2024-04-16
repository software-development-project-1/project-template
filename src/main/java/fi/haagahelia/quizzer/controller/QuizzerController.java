package fi.haagahelia.quizzer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
// import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fi.haagahelia.quizzer.model.Difficulty;
import fi.haagahelia.quizzer.model.Quizz;
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

    //show all quizzes
   @RequestMapping(value= "/quizzlist")
   public String recipientList(Model model) {
       model.addAttribute("quizzlist", quizzRepository.findAll());
       return "quizzlist";
   }

}
