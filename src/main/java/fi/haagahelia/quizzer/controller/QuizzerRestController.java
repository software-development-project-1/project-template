package fi.haagahelia.quizzer.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fi.haagahelia.quizzer.model.Question;
import fi.haagahelia.quizzer.model.Status;
import fi.haagahelia.quizzer.repository.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fi.haagahelia.quizzer.model.Quizz;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Tag(name = "Quizzer", description = "Operations for accessing and managing the quizzes")
public class QuizzerRestController {

    @Autowired
    private QuizzRepository quizzRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private QuestionRepository questionRepository;

    // show all quizzes
    @GetMapping("/quizzlist")
    public List<Quizz> showAllQuizz() {
        return (List<Quizz>) quizzRepository.findAll();
    }

    @GetMapping("/publishedquizz")
    public List<Quizz> getPublishedQuizzNewestToOldest() {

        // Fetch the list of quizzes with a status of true (published)
        Status status = statusRepository.findByStatus(true);
        List<Quizz> publishedQuizzes = quizzRepository.findByStatus(status);

        // Sort the list by creation time in descending order
        Collections.sort(publishedQuizzes, Comparator.comparing(Quizz::getCreationTime).reversed());

        // Return the sorted list
        return publishedQuizzes;
    }

    @GetMapping("/questionlist")
    public List<Question> getQuestionList() {
        // Return question list
        return (List<Question>) questionRepository.findAll();
    }

}