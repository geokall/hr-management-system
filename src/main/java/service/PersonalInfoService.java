package service;

import dto.EducationDTO;
import dto.PersonalInformationDTO;

public interface PersonalInfoService {

    PersonalInformationDTO findPersonalInfo(Long id);

    void updatePersonalInfo(Long id, PersonalInformationDTO dto);

    void createEducation(Long id, EducationDTO dto);

    void updateEducation(Long id, EducationDTO dto);

    void deleteEducation(Long id);
}
