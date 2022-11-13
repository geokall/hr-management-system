package service;

import dto.MainInfoDTO;
import dto.PersonalInfoDTO;

public interface UserService {

    PersonalInfoDTO findPersonalInfo(Long id);

    MainInfoDTO findMainInfo(Long id);

    void updateBasicInformation(Long id, PersonalInfoDTO dto);

    void inviteUser(String email);
}
