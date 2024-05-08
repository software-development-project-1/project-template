package fi.haagahelia.quizzer.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRegistrationDto {

    @NotEmpty(message = "Please provide a user name with at least 3 characters")
    @Size(min=3)
    private String username = "";

    @NotEmpty(message = "Please provide a password with at least 8 characters")
    @Size(min=8)
    private String password = "";

    @NotEmpty(message = "Please retype your password")
    @Size(min=8)
    private String passwordCheck = "";
    
}
