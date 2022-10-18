package dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class RegisterDTO {

    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String roleName;

    private String name;

    private String surname;

    private Date birthDate;

    private LocalDateTime dateCreated;

    private LocalDateTime lastModificationDate;
}
