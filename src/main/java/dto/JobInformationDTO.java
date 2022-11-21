package dto;

import lombok.Data;

import java.util.List;

@Data
public class JobInformationDTO {

    private Long id;

    private List<BonusDTO> bonuses;
}
