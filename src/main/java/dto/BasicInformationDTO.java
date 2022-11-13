package dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BasicInformationDTO {

    private Long id;

    private String name;

    private String surname;

    private String businessEmail;

    private String personalEmail;

    private Long employeeNumber;

    private String username;

    private String birthDate;

    private String hireDate;

    private String mobileNumber;

    private String workNumber;

    private String homeNumber;

    private String vatNumber;

    private LocalDateTime createdDate;

    private LocalDateTime lastModificationDate;

    private String gender;

    private String employeeStatus;

    private String maritalStatus;

    private String linkedinUrl;

    private String twitterUrl;

    private String facebookUrl;

    private String street1;

    private String street2;

    private String city;

    private String province;

    private String postalCode;

    private String country;
}
