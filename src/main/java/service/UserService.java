package service;

import dto.UserDTO;

public interface UserService {

    UserDTO findUserInfo(Long id);
}
