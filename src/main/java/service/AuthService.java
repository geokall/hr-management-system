package service;

import dto.LoginDTO;
import security.JwtResponseDTO;

public interface AuthService {

    JwtResponseDTO login(LoginDTO dto);

}
