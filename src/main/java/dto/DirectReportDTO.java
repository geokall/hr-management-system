package dto;

import lombok.Data;

@Data
public class DirectReportDTO {

    private Long id;

    private Long directReportUserId;

    private String name;

    private String surname;
}
