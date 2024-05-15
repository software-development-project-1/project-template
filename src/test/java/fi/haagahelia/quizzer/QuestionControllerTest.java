package fi.haagahelia.quizzer;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;

import fi.haagahelia.quizzer.model.Question;
import fi.haagahelia.quizzer.model.Quiz;
import fi.haagahelia.quizzer.repository.QuestionRepository;
import fi.haagahelia.quizzer.repository.QuizRepository;


@SpringBootTest
@AutoConfigureMockMvc
public class QuestionControllerTest {
    @Autowired
    QuizRepository quizRepository ;

    @Autowired
    QuestionRepository questionRepository ;

     @Autowired
    private MockMvc mockMvc;
   

@BeforeEach
    void setUp() throws Exception {
        quizRepository.deleteAll();
        questionRepository.deleteAll();
    }

    @Test
    public void getQuestionsByQuizIdReturnsEmptyListWhenQuizDoesNotHaveQuestions () throws Exception{
            Quiz quiz1= new Quiz (null, Instant.now(), "Quiz for test1", "Test1 description", true, null);
            quizRepository.save(quiz1);
            this.mockMvc.perform(get("/api/QuizApp/quiz/" + quiz1.getId()+"/questions"))
            .andExpect(status().isNotFound());
    }


    


}

