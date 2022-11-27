package dto;

import lombok.Data;

@Data
public class BonusDTO {

    private Long id;

    private String bonusDate;

    private Double amount;

    private String comment;
}
