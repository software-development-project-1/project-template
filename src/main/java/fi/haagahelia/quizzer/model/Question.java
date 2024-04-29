package fi.haagahelia.quizzer.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long questionId;
    @NotEmpty(message = "Please provide a question")
    @Column(nullable = false)
    private String questionText;
    @NotEmpty(message = "Please provide a correct answer")
    @Column(nullable = false)
    private String correctAnswer;
    @NotEmpty(message = "Please provide a difficulty level")
    @Column(nullable = false)
    private String difficultyLevel;

    @ManyToOne
    @JoinColumn(name = "quizId")
    private Quiz quiz;

    @JsonIgnore 
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
    private List<Answer> answers;

    public Question(String questionText, String correctAnswer, String difficultyLevel, Quiz quiz) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.difficultyLevel = difficultyLevel;
        this.quiz = quiz;
    }

    public Question(String questionText,String correctAnswer, String difficultyLevel, Quiz quiz, List<Answer> answers) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.difficultyLevel = difficultyLevel;
        this.quiz = quiz;
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "Question [questionId=" + questionId + ", questionText=" + questionText + ", correctAnswer="
                + correctAnswer + ", difficultyLevel=" + difficultyLevel + ", quiz=" + quiz.getId()+ "]";
    }

}
