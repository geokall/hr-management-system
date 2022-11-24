package dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class BonusDTO {

    private Long id;

    @JsonFormat(pattern = "dd/mm/yy")
    private Date bonusDate;

    private Double amount;

    private String comment;
}
