package fi.haagahelia.quizzer.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnswerDto {

    private Long answerDtoId;
    @NotEmpty(message = "Please provide an answer")
    private String answerText;
    private Long questionId;

    public AnswerDto(Long answerDtoId, String answerText, Long questionId) {
        this.answerDtoId = answerDtoId;
        this.answerText = answerText;
        this.questionId = questionId;
    }



}
