package fi.haagahelia.quizzer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
// import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;

import fi.haagahelia.quizzer.model.Question;
import fi.haagahelia.quizzer.model.Quizz;
import fi.haagahelia.quizzer.repository.DifficultyRepository;
import fi.haagahelia.quizzer.repository.QuestionRepository;
import fi.haagahelia.quizzer.repository.QuizzRepository;
import jakarta.persistence.EntityNotFoundException;

@Controller
public class QuestionController {

    @Autowired
    private QuizzRepository quizzRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private DifficultyRepository difficultyRepository;


    @GetMapping("/questionlist/{quizzId}")
    public String questionList(@PathVariable("quizzId") Long quizzId, Model model) {
        Quizz quizz = quizzRepository.findById(quizzId)
                .orElseThrow(() -> new EntityNotFoundException("project not found"));
        List<Question> questions = quizz.getQuestion();
        model.addAttribute("quizzName", quizz.getName().toUpperCase());
        model.addAttribute("questions", questions);
        return "questionlist";
    }

    @GetMapping(value = "/editquestion/{questionId}")
    public String editQuizForm(@PathVariable("questionId") Long questionId, Model model) {
        model.addAttribute("question", questionRepository.findById(questionId));
        model.addAttribute("difficulties", difficultyRepository.findAll());
        model.addAttribute("quizzes", quizzRepository.findAll());
        return "editquestion.html";
    }

    @PostMapping(value = "/savequestion")
    public String save(Question question) {
        questionRepository.save(question);
        return "redirect:/questionlist/" + question.getQuizz().getQuizzId();
    }

}