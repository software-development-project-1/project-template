package fi.haagahelia.quizzer;

import fi.haagahelia.quizzer.model.Category;
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


@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    QuizRepository quizRepository;
    @Autowired
    private MockMvc mockMvc;
    @BeforeEach
    void setUp() throws Exception {
        categoryRepository.deleteAll();
    }
    @Test
    public void getQuizzesByCategoryReturnsEmptyListWhenNoQuizzesExistForCategory() throws Exception {
        Category category = new Category("Category 1");
        categoryRepository.save(category);
        MvcResult result = this.mockMvc.perform(get("/api/QuizApp/quizes?categoryId=" + category.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }
    @Test
    public void getQuizzesByCategoryReturnsListOfQuizzesWhenQuizzesExistForCategory() throws Exception {
        Category category = new Category("Category 1");
        categoryRepository.save(category);
        Quiz quiz1 = new Quiz(category, Instant.now(), "Quiz 1", "Description 1", true, null);
        Quiz quiz2 = new Quiz(category, Instant.now(), "Quiz 2", "Description 2", true, null);
        quizRepository.saveAll(List.of(quiz1, quiz2));
        MvcResult result = this.mockMvc.perform(get("/api/QuizApp/quizes?categoryId=" + category.getId()))
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        assertTrue(responseBody.contains("\"quizName\":\"Quiz 1\""));
        assertTrue(responseBody.contains("\"quizDescription\":\"Description 1\""));
    }

    @Test
    public void getQuizzesByCategoryReturnsNotFoundErrorWhenCategoryDoesNotExist() throws Exception {
        this.mockMvc.perform(get("/api/QuizApp/quizes?categoryId=1000"))
                .andExpect(status().isNotFound());
    }
    @Test
    public void getAllCategoriesReturnsEmptyListWhenNoCategoriesExist() throws Exception {
        this.mockMvc.perform(get("/api/QuizApp/categories"))
                .andExpect(status().isNotFound());
    }
    @Test
    public void getAllCategoriesReturnsListOfCategoriesWhenCategoriesExist() throws Exception {
        Category category1 = new Category("Category 1");
        Category category2 = new Category("Category 2");
        categoryRepository.saveAll(List.of(category1, category2));
        MvcResult result = this.mockMvc.perform(get("/api/QuizApp/categories"))
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        assertTrue(responseBody.contains("Category 1"));
        assertTrue(responseBody.contains("Category 2"));
    }
}