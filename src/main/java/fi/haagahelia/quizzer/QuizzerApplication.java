package fi.haagahelia.quizzer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import fi.haagahelia.quizzer.repository.CategoryRepository;
import fi.haagahelia.quizzer.repository.DifficultyRepository;
import fi.haagahelia.quizzer.repository.QuestionRepository;
import fi.haagahelia.quizzer.repository.QuizzRepository;
import fi.haagahelia.quizzer.repository.StatusRepository;

@SpringBootApplication
public class QuizzerApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizzerApplication.class, args);
	}

	@Bean
	public CommandLineRunner DBlinerunner(CategoryRepository categoryRepository,
			DifficultyRepository difficultyRepository, QuestionRepository questionRepository,
			QuizzRepository quizzRepository, StatusRepository statusRepository) {
		return (args) -> {

		};
	}

}
