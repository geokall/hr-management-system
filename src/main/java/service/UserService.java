package service;

import dto.UserDTO;

public interface UserService {

    UserDTO findUserInfo(Long id);

    void updateUserInfo(Long id, UserDTO dto);

    void inviteUser(String email);
}
