package dto;

import lombok.Data;

import java.util.Date;

@Data
public class EducationDTO {

    private String college;

    private String degree;

    private String specialization;

    private double gpa;

    private Date studyFrom;

    private Date studyTo;
}
