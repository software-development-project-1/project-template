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
public class Status {
    @Id
    @GeneratedValue
    private Long statusId;
    
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "status")
    private List<Quizz> quizzes;

    @Column(nullable = false)
    private Boolean status;

    public Status(){}
    public Status(boolean status){
        this.status = status;
    }

    //getter
    public Long getStatusId(){
        return statusId;
    }
    public Boolean getStatus(){
        return status;
    }
    public List<Quizz> getQuizzes(){
        return quizzes;
    }
    //setter
    public void setStatus(boolean status){
        this.status = status;
    }
    public void setQuizzes(List<Quizz> quizzes){
        this.quizzes = quizzes;
    }
    public void setStatusId(Long statusId){
        this.statusId = statusId;
    }
    

}
