package dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {

    private Long id;

    private String name;

    private String surname;

    private String email;

    private String username;

    private String birthDate;

    private String mobileNumber;

    private String vatNumber;

    private LocalDateTime createdDate;

    private LocalDateTime lastModificationDate;

    private String role;
}
