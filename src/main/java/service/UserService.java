package service;

import dto.MainInfoDTO;

public interface UserService {

    MainInfoDTO findMainInfo(Long id);

    void inviteUser(String email);
}
