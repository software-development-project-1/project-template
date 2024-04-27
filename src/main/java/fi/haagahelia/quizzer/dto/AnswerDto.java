package fi.haagahelia.quizzer.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnswerDto {

    @NotEmpty(message = "Please provide an answer")
    private String answerText;
    private Long questionId;

    public AnswerDto( String answerText, Long questionId) {
        this.answerText = answerText;
        this.questionId = questionId;
    }



}
