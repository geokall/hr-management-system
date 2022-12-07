package service;

import dto.EducationDTO;
import dto.PersonalInformationDTO;

import java.util.List;

public interface PersonalInfoService {

    PersonalInformationDTO findPersonalInfo(Long id);

    List<EducationDTO> findEducations(Long id);

    void updatePersonalInfo(Long id, PersonalInformationDTO dto);

    void createEducation(Long id, EducationDTO dto);

    void updateEducation(Long id, EducationDTO dto);

    void deleteEducation(Long id);
}
