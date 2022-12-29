package service;

import dto.MainInfoDTO;
import dto.PasswordDTO;

public interface UserService {

    MainInfoDTO findMainInfo(Long id);

    void inviteUser(String email);

    void changeUserPassword(Long id, PasswordDTO dto);
}
