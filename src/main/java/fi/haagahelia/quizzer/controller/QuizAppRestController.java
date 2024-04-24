package fi.haagahelia.quizzer.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
        List<Category> categoryList = categoryRepository.findAllByOrderByNameAsc();

        if (!categoryList.isEmpty()) {
             return categoryList;
        }

        throw new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Categories were not not found"
        );
       
    }

    // Example of the link with not required published parameter to get non published quizzes
    // http://localhost:8080/api/QuizApp/quizes?published=false
    @GetMapping("/quizes")
    public @ResponseBody List<Quiz> getQuizes(@RequestParam(required = false) Boolean published) {
        List<Quiz> quizList;
        if (published == null) {
            quizList = quizRepository.findAllByOrderByQuizNameAsc();
        } else {
            quizList = quizRepository.findByPublishedOrderByCreatedAtDesc(published);
        }

        if (!quizList.isEmpty()) {
            return quizList;
        }

        throw new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Quizes were not not found"
        );
    }

    @GetMapping("/quiz/{id}")
    public @ResponseBody Quiz getQuizById(@PathVariable("id") Long id) {
        Optional<Quiz> existingQuizOptional = quizRepository.findById(id);
        if (existingQuizOptional.isPresent()) {
            Quiz existingQuiz = existingQuizOptional.get();
            return existingQuiz;
        }	
        throw new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Quiz with id: "+ id + " not found"
        );
    }
}
