package service;

import dto.LoginDTO;
import dto.RegisterDTO;
import security.JwtResponseDTO;

public interface AuthService {

    JwtResponseDTO login(LoginDTO dto);

    Long register(RegisterDTO dto);

}
