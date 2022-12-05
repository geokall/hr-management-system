package dto;

import lombok.Data;

@Data
public class EducationDTO {

    private Long id;

    private String college;

    private String degree;

    private String specialization;

    private double gpa;

    private String studyFrom;

    private String studyTo;
}
