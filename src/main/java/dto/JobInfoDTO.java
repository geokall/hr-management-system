package dto;

import lombok.Data;

import java.util.List;

@Data
public class JobInfoDTO {

    private String ethnicity;

    private String jobCategory;

    private String hireDate;

    private String jobDescription;

    private List<BonusDTO> bonuses;
}
