package service;

import dto.LoginDTO;
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

    private JwtClaimService jwtClaimService;
    private HuaUserRepository huaUserRepository;
    private HuaRoleRepository huaRoleRepository;

    @Inject
    public AuthServiceImpl(JwtClaimService jwtClaimService,
                           HuaUserRepository huaUserRepository,
                           HuaRoleRepository huaRoleRepository) {
        this.jwtClaimService = jwtClaimService;
        this.huaUserRepository = huaUserRepository;
        this.huaRoleRepository = huaRoleRepository;
    }

    @Override
    public JwtResponseDTO login(LoginDTO dto) {
        HuaUser huaUser = huaUserRepository.findByUsernameAndPassword(dto.getUsername(), dto.getPassword())
                .orElseThrow(() -> {
                    throw new WebApplicationException(Response.status(404)
                            .entity("User not found")
                            .build());
                });

        String userRole = huaRoleRepository.findUserRole(huaUser.getId())
                .stream()
                .findAny()
                .orElse(null);

        String jwt = jwtClaimService.generateUserToken(huaUser.getUsername(), userRole);

        JwtResponseDTO jwtResponseDTO = new JwtResponseDTO();
        jwtResponseDTO.setId(huaUser.getId());
        jwtResponseDTO.setEmail(huaUser.getEmail());
        jwtResponseDTO.setUsername(huaUser.getUsername());
        jwtResponseDTO.setToken(jwt);
        jwtResponseDTO.setRole(userRole);

        return jwtResponseDTO;
    }
}
