package dto;

import lombok.Data;

@Data
public class UserDTO {

    private Long id;

    private String username;

    private String name;

    private String surname;

    private String workNumber;

    private String businessEmail;

    private String linkedinUrl;

    private DivisionDTO division;

    private LocationDTO location;

}
