package fi.haagahelia.quizzer.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnswerDto {

    private Long id;
    @NotEmpty(message = "Please provide an answer")
    private String answerText;
    private Long questionId;

    public AnswerDto(Long id, String answerText, Long questionId) {
        this.id = id;
        this.answerText = answerText;
        this.questionId = questionId;
    }



}
