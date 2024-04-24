package fi.haagahelia.quizzer.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import fi.haagahelia.quizzer.repository.CategoryRepository;
import fi.haagahelia.quizzer.repository.QuizRepository;
import fi.haagahelia.quizzer.model.Category;
import fi.haagahelia.quizzer.model.Quiz;


@RestController
@RequestMapping("/api/QuizApp")
@CrossOrigin(origins = "*")
public class QuizAppRestController {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private QuizRepository quizRepository;

    @GetMapping("/categories")
    public @ResponseBody List<Category> getCategories() {
        return categoryRepository.findAllByOrderByNameAsc();
    }

    @GetMapping("/quizes")
    public @ResponseBody List<Quiz> getQuizes() {
        return quizRepository.findAllByOrderByNameAsc();
    }

    @GetMapping("/quiz/{id}")
    public @ResponseBody Quiz getQuizById(@PathVariable("id") Long id) {
        Optional<Quiz> existingQuizOptional = quizRepository.findById(id);
        if (existingQuizOptional.isPresent()) {
            Quiz existingQuiz = existingQuizOptional.get();
            return existingQuiz;
        }	
    
        return null;
    }
}
