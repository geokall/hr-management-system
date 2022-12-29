package dto;

import lombok.Data;

@Data
public class PasswordDTO {

    private String currentPassword;

    private String password;

    private String samePassword;
}
