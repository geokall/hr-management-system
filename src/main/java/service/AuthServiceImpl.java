package service;

import dto.LoginDTO;
import entity.HuaUser;
import io.quarkus.elytron.security.common.BcryptUtil;
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
        HuaUser huaUser = fetchUserBy(dto.getUsername());

        checkHashedPassword(huaUser, dto);

        String userRole = fetchUserRoleBy(huaUser);

        String jwt = jwtClaimService.generateUserToken(huaUser.getUsername(), userRole);

        return JwtResponseDTO.builder()
                .id(huaUser.getId())
                .email(huaUser.getEmail())
                .username(huaUser.getUsername())
                .token(jwt)
                .role(userRole)
                .build();
    }


    private void checkHashedPassword(HuaUser huaUser, LoginDTO dto) {
        String hashedPassword = huaUser.getPassword();
        String password = dto.getPassword();

        boolean matches = BcryptUtil.matches(password, hashedPassword);

        if (!matches) {
            throw new WebApplicationException(Response.status(404)
                    .entity("Invalid credentials")
                    .build());
        }
    }

    private String fetchUserRoleBy(HuaUser huaUser) {
        return huaRoleRepository.findUserRole(huaUser.getId())
                .stream()
                .findAny()
                .orElse(null);
    }

    private HuaUser fetchUserBy(String username) {
        return huaUserRepository.findByUsername(username)
                .orElseThrow(() -> {
                    throw new WebApplicationException(Response.status(404)
                            .entity("Invalid credentials")
                            .build());
                });
    }
}
