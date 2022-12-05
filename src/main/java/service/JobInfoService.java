package service;

import dto.BonusDTO;
import dto.EducationDTO;
import dto.JobInformationDTO;

public interface JobInfoService {

    void createBonus(Long id, BonusDTO dto);

    void updateBonus(Long id, BonusDTO dto);

    void deleteBonus(Long id);

    void updateJobInfo(Long id, JobInformationDTO dto);

    JobInformationDTO fetchJobInformation(Long id);

    void createEducation(Long id, EducationDTO dto);

    void updateEducation(Long id, EducationDTO dto);

    void deleteEducation(Long id);
}
