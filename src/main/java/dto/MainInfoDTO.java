package dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
public class MainInfoDTO {

    private Long id;

    private String name;

    private String surname;

    private String jobTitle;

    private String workNumber;

    private String mobileNumber;

    private String businessEmail;

    private Date hireDate;

    private LocalDate countDate;

    private int countYears;

    private int countMonths;

    private int countDays;

    private Long employeeNumber;

    private String jobStatus;

    private String division;

    private String location;

    private ManagerDTO directManager;

    private List<DirectReportDTO> directReports;
}
