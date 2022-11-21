package dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BonusDTO {

    private Long id;

    private LocalDate bonusDate;

    private Double amount;

    private String comment;
}
