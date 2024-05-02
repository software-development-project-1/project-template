package fi.haagahelia.quizzer.controller;

import fi.haagahelia.quizzer.model.Question;
import fi.haagahelia.quizzer.model.Quiz;
import fi.haagahelia.quizzer.repository.QuestionRepository;
import fi.haagahelia.quizzer.repository.QuizRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class QuestionController {
    @Autowired
    private QuizRepository qrepository;
    @Autowired
    private QuestionRepository questionrepository;
    @RequestMapping(value = "/addquestiontolist/{id}", method = RequestMethod.GET)
    public String addQuestions(@PathVariable("id") Long id, Model model) {
        Optional<Quiz> quizOptional = qrepository.findById(id);
        if (quizOptional.isPresent()) {
            Quiz quiz = quizOptional.get();
            Question newQuestion = new Question();
            newQuestion.setQuiz(quiz);
            model.addAttribute("newQuestion", newQuestion);
            model.addAttribute("quiz", quiz);
            return "addQuestionToList";
        } else {
            System.out.println("Something went wrong");
            return "error";
        }
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
    public String saveQuestionToList(@Valid @ModelAttribute("newQuestion") Question newQuestion, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
        model.addAttribute("newQuestion", newQuestion);
        if (newQuestion.getQuiz() != null) {
            model.addAttribute("quiz", newQuestion.getQuiz());
        }
        return "addQuestionToList";
    }
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
