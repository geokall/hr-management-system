package dto;

import lombok.Data;

@Data
public class ManagerDTO {

    private Long id;

    private byte[] profilePicture;

    private Long managerId;

    private String name;

    private String surname;

    private String titleJob;
}
