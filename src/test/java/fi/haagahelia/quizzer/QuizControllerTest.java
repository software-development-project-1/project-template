package fi.haagahelia.quizzer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fi.haagahelia.quizzer.model.Quiz;
import fi.haagahelia.quizzer.repository.CategoryRepository;
import fi.haagahelia.quizzer.repository.QuizRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@AutoConfigureMockMvc
public class QuizControllerTest {
    @Autowired
    QuizRepository quizRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    private MockMvc mockMvc;
    ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    @BeforeEach
    void setUp() throws Exception {
        quizRepository.deleteAll();
    }
    @Test
    public void getAllQuizzesReturnsEmptyListWhenNoQuizzesExist() throws Exception {
        this.mockMvc.perform(get("/api/QuizApp/quizes"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
    @Test
    public void getAllQuizzesReturnsListOfPublishedQuizzesWhenQuizzesExist() throws Exception {
        // Save a few quizzes, some published and some not published
        Quiz quiz1 = new Quiz(null, Instant.now(), "Quiz 1", "Description 1", true, null);
        Quiz quiz2 = new Quiz(null, Instant.now(), "Quiz 2", "Description 2", false, null);
        Quiz quiz3 = new Quiz(null, Instant.now(), "Quiz 3", "Description 3", true, null);
        quizRepository.saveAll(List.of(quiz1, quiz2, quiz3));
        MvcResult result = this.mockMvc.perform(get("/api/QuizApp/quizes"))
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        assertTrue(responseBody.contains("Quiz 1"));
        assertTrue(responseBody.contains("Quiz 3"));
        assertTrue(responseBody.contains("Quiz 2"));
    }
    @Test
    public void getQuizByIdReturnsPublishedQuizWhenQuizExists() throws Exception {
        Quiz quiz = new Quiz(null, Instant.now(), "Published Quiz", "Description", true, null);
        quizRepository.save(quiz);
        MvcResult result = this.mockMvc.perform(get("/api/QuizApp/quiz/" + quiz.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":" + quiz.getId() + ",\"category\":null,\"createdAt\":\"" + quiz.getCreatedAt() + "\",\"quizName\":\"Published Quiz\",\"quizDescription\":\"Description\",\"published\":true,\"user\":null}"))
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        Quiz returnedQuiz = mapper.readValue(responseBody, Quiz.class);
        assertTrue(returnedQuiz.getQuizName().equals("Published Quiz"));
        assertTrue(returnedQuiz.getQuizDescription().equals("Description"));
        assertTrue(returnedQuiz.getPublished());
        assertTrue(returnedQuiz.getQuestions().isEmpty());
    }

    @Test
    public void getQuizByIdReturnsErrorWhenQuizDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/QuizApp/quiz/999"))
                .andExpect(status().isNotFound());
    }
    @Test
    public void getQuizByIdReturnsErrorWhenQuizIsNotPublished() throws Exception {
        Quiz quiz = new Quiz(null, Instant.now(), "Non-Published Quiz", "Description", false, null);
        quizRepository.save(quiz);
        mockMvc.perform(get("/api/QuizApp/quiz/" + quiz.getId()))
                .andExpect(status().isNotFound());
    }
}