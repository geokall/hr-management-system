package dto;

import lombok.Data;

@Data
public class WorkInformationDTO {

    private Long id;

    private String jobTitle;

    private LocationDTO location;

    private DivisionDTO division;

    private ManagerDTO manager;

    private ManagerDTO user;
}
