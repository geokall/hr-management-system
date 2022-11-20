package service;

import dto.InformationDTO;
import dto.MainInfoDTO;

public interface UserService {

    InformationDTO findPersonalInfo(Long id);

    MainInfoDTO findMainInfo(Long id);

    void updateBasicInformation(Long id, InformationDTO dto);

    void inviteUser(String email);
}
