package dto;

import lombok.Data;

import java.util.List;

@Data
public class JobInfoDTO {

    private List<BonusDTO> bonuses;
}
