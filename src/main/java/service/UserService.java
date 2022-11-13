package service;

import dto.PersonalInfoDTO;

public interface UserService {

    PersonalInfoDTO findPersonalInfo(Long id);

    void updateBasicInformation(Long id, PersonalInfoDTO dto);

    void inviteUser(String email);
}
