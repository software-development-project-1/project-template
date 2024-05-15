package fi.haagahelia.quizzer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private Long reviewId;
    private String username;
    private Integer rating;
    private String review;
}
