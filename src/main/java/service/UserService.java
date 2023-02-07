package service;

import dto.MainInfoDTO;
import dto.PasswordDTO;

import javax.ws.rs.core.UriInfo;

public interface UserService {

    MainInfoDTO findMainInfo(Long id);

    void inviteUser(String email, UriInfo uriInfo);

    void inviteManager(String username, Long managerId);

    void changeUserPassword(Long id, PasswordDTO dto);
}
