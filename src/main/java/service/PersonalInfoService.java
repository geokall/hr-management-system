package service;

import dto.PersonalInformationDTO;

public interface PersonalInfoService {

    PersonalInformationDTO findPersonalInfo(Long id);

    void updatePersonalInfo(Long id, PersonalInformationDTO dto);

}
