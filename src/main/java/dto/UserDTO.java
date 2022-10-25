package dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class UserDTO {

    private Long id;

    private String name;

    private String surname;

    private String email;

    private String username;

    private Date birthDate;

    private LocalDateTime createdDate;

    private LocalDateTime lastModificationDate;

    private String role;
}
