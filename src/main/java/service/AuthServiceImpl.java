package service;

import dto.LoginDTO;
import entity.HuaRole;
import entity.HuaUser;
import repository.HuaUserRepository;
import repository.HuaRoleRepository;
import security.JwtClaimService;
import security.JwtResponseDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class AuthServiceImpl implements AuthService {

    @Inject
    JwtClaimService service;

    @Inject
    HuaUserRepository huaUserRepository;

    @Inject
    HuaRoleRepository huaRoleRepository;

    @Override
    public JwtResponseDTO login(LoginDTO dto) {
        HuaUser huaUser = huaUserRepository.findByUsernameAndPassword(dto.getUsername(), dto.getPassword())
                .orElseThrow(() -> {
                    throw new WebApplicationException(Response.status(404)
                            .entity("User not found")
                            .build());
                });

        String jwt = service.generateReaderToken(huaUser.getUsername(), "test");

        JwtResponseDTO jwtResponseDTO = new JwtResponseDTO();
        jwtResponseDTO.setId(huaUser.getId());
        jwtResponseDTO.setEmail(huaUser.getEmail());
        jwtResponseDTO.setUsername(huaUser.getUsername());
        jwtResponseDTO.setToken(jwt);

//        String role = huaRoleRepository.findByRoles_Id(huaUser.getId())
//                .stream()
//                .map(HuaRole::getName)
//                .findAny().orElse(null);
//
//        jwtResponseDTO.setRole(role);

        return jwtResponseDTO;
    }
}
