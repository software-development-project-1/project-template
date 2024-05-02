package fi.haagahelia.quizzer.service;

import fi.haagahelia.quizzer.dto.AnswerDto;
import fi.haagahelia.quizzer.model.Answer;
import fi.haagahelia.quizzer.model.Question;

public class mappingDTO {
     
    public Answer toEntity(AnswerDto dto) {
        Answer answer = new Answer();
        answer.setAnswerText(dto.getAnswerText());
        answer.setCorrectness(false);
        Question question = new Question();
        question.setQuestionId(dto.getQuestionId());
        answer.setQuestion(question);
    
        return answer;
    }
    
}
