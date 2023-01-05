package dto;

import lombok.Data;

@Data
public class WorkInformationDTO {

    private Long id;

    private String effectiveDate;

    private String jobTitle;

    private String jobStatus;

    private IdNameDTO location;

    private IdNameDTO division;

    private IdNameDTO manager;
}
