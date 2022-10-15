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

    private final JwtClaimService jwtClaimService;
    private final HuaUserRepository huaUserRepository;
    private final HuaRoleRepository huaRoleRepository;

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

        return JwtResponseDTO.builder()
                .id(huaUser.getId())
                .email(huaUser.getEmail())
                .username(huaUser.getUsername())
                .token(jwt)
                .role(userRole)
                .build();
    }
}
