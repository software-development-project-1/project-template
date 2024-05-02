package fi.haagahelia.quizzer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAnswerDto {
    private Long questionId;
    private String questionText;
    private String answerText;
    private String difficultyLevel;
    private int totalAnswers;
    private int correctAnswers;
    private int wrongAnswers;
    private double correctPercentage;

    
}
