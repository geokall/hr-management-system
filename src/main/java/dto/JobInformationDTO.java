package dto;

import lombok.Data;

import java.util.List;

@Data
public class JobInformationDTO {

    private Long id;

    private String hireDate;

    private String ethnicity;

    private String jobCategory;

    private String jobDescription;

    private List<BonusDTO> bonuses;
}
