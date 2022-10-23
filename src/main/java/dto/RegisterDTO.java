package dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class RegisterDTO {

    private Long id;

    private String surname;

    private String name;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String email;

    private Date birthDate;

    private LocalDateTime dateCreated;

    private LocalDateTime lastModificationDate;
}
