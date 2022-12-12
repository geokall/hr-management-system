package dto;

import lombok.Data;

@Data
public class WorkInformationDTO {

    private Long id;

    private String jobTitle;

    private IdNameDTO location;

    private IdNameDTO division;

    private IdNameDTO manager;

    private IdNameDTO user;
}
