package service;

import dto.InformationDTO;

public interface PersonalInfoService {

    InformationDTO findPersonalInfo(Long id);

    void updateBasicInformation(Long id, InformationDTO dto);

}
