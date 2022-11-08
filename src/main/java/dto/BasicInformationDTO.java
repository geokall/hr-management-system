package dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BasicInformationDTO {

    private Long id;

    private String role;

    private String name;

    private String surname;

    private String email;

    private String username;

    private String birthDate;

    private String hireDate;

    private String mobileNumber;

    private String vatNumber;

    private LocalDateTime createdDate;

    private LocalDateTime lastModificationDate;

    private String gender;

    private String employeeStatus;

    private String jobStatus;

    private String maritalStatus;
}
