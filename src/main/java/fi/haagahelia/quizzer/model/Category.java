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
public class Category {
    @Id
    @GeneratedValue
    private Long categoryId;
    
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    private List<Quizz> quizzes;

    public Category(){}
    public Category(String name, String description){
        super();
        this.name = name;
        this.description = description;
    }
    // getter
    public Long getCategoryId(){
        return categoryId;
    }
    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }
    public List<Quizz> getQuizzes(){
        return quizzes;
    }

    // setter
    public void setCategoryId(Long categoryId){
        this.categoryId = categoryId;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public void setQuizzes(List<Quizz> quizzes){
        this.quizzes = quizzes;
    }


    @Override
    public String toString(){
        return "Category [ Name = "+getName()+", Description = "+ getDescription()+" ]";
    }
}
