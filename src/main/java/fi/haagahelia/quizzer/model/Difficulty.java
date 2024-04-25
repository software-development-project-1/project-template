package fi.haagahelia.quizzer.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Difficulty {
    @Id
    @GeneratedValue
    private Long difficultyId;

    @Column(nullable = false)
    private String level;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "difficulty")
    private List<Question> questions;

    public Difficulty(){}
    public Difficulty(String level){
        this.level=level;
    }

    //getter
    public Long getDifficultyId(){
        return difficultyId;
    }
    public String getLevel(){
        return level;
    }
    

    @JsonIgnore
    public List<Question> getQuestion(){
        return questions;
    }


    //setter
    public void setDifficultyId(Long difficultyId){
        this.difficultyId = difficultyId;
    }
    public void setLevel(String level){
        this.level=level;
    }

    @JsonIgnore
    public void setQuestions(List<Question> questions){
        this.questions=questions;
    }



    @JsonIgnore
    @Override
    public String toString(){
        return "Difficulty level = " + getLevel();
    }
    
}
