package recipes.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.*;

@Entity
@Getter
@Setter
public class User {

    private static final String emailRegex = "(?i)[\\w!#$%&'*+/=?`{|}~^-]+" +
            "(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-z0-9-]+\\.)+[a-z]{2,6}";

    @Id
    @NonNull
    @NotBlank
    @Email(regexp = emailRegex)
    private String email;

    @NonNull
    @NotBlank
    @Size(min = 8)
    private String password;

}