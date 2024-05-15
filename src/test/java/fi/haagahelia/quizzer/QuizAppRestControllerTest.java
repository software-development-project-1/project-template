package fi.haagahelia.quizzer;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.haagahelia.quizzer.dto.AnswerDto;
import fi.haagahelia.quizzer.model.Answer;
import fi.haagahelia.quizzer.model.Question;
import fi.haagahelia.quizzer.model.Quiz;
import fi.haagahelia.quizzer.repository.QuizRepository;
import fi.haagahelia.quizzer.repository.AnswerRepository;
import fi.haagahelia.quizzer.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class QuizAppRestControllerTest {

    @Autowired
    QuizRepository quizRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    private MockMvc mockMvc;
    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws Exception {
        answerRepository.deleteAll();
        questionRepository.deleteAll();
        quizRepository.deleteAll();
    }

    @Test
    public void createAnswerSavesValidAnswer() throws Exception{

        //create an published quiz with a question for adding answer from student dashboard
        Quiz quiz1 = new Quiz(null, Instant.now(), "Quiz 1", "Description 1", true, null);
        quizRepository.save(quiz1);
        Question question1 = new Question("Question 1", "Valid", "Easy", quiz1);
        questionRepository.save(question1);

        AnswerDto answerDto = new AnswerDto("Valid", question1.getQuestionId());
        String requestBody = mapper.writeValueAsString(answerDto);

        this.mockMvc.perform(post("/api/QuizApp/questions/" + question1.getQuestionId() + "/answers")
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.correctness").value("true"));
        boolean isCorrect = question1.getCorrectAnswer().equalsIgnoreCase(answerDto.getAnswerText().trim());
        Answer newAnswer = new Answer(answerDto.getAnswerText(), isCorrect, question1);
        answerRepository.save(newAnswer);

        List<Answer> answers = answerRepository.findAll();
        assertEquals(2, answers.size());
        assertEquals("Valid", answers.get(0).getAnswerText());
        assertTrue(answers.get(0).isCorrectness());

    }

}
