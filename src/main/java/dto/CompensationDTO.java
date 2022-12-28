package dto;

import lombok.Data;

@Data
public class CompensationDTO {

    private Long id;

    private String effectiveDate;

    private String payType;

    private Double payRate;

    private String comment;
}
