package service;

import dto.LoginDTO;
import dto.RegisterDTO;
import entity.HuaUser;
import io.quarkus.elytron.security.common.BcryptUtil;
import lombok.Data;
import org.springframework.util.ObjectUtils;
import repository.HuaUserRepository;
import repository.HuaRoleRepository;
import security.JwtClaimService;
import security.JwtResponseDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.sql.Date;
import java.time.LocalDateTime;

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

    @Override
    public Long register(RegisterDTO dto) {

        handleDuplicates(dto);

        HuaUser huaUser = new HuaUser();
        huaUser.setName(dto.getName());
        huaUser.setBirthDate(dto.getBirthDate());
        huaUser.setEmail(dto.getEmail());
        huaUser.setDateCreated(LocalDateTime.now());
        huaUser.setSurname(dto.getSurname());
        huaUser.setUsername(dto.getUsername());
        BcryptUtil.bcryptHash(dto.getPassword());

        huaRoleRepository.findByName(dto.getRoleName())
                .ifPresent(huaUser::addRole);

        HuaUser savedUser = huaUserRepository.save(huaUser);

        return savedUser.getId();
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

    private void handleDuplicates(RegisterDTO dto) {
        if (!ObjectUtils.isEmpty(dto.getEmail())) {
            huaUserRepository.findByEmail(dto.getEmail())
                    .ifPresent(user -> {
                        throw new WebApplicationException(Response.status(404)
                                .entity("Email already exists")
                                .build());
                    });
        }

        if (!ObjectUtils.isEmpty(dto.getUsername())) {
            huaUserRepository.findByUsername(dto.getUsername())
                    .ifPresent(user -> {
                        throw new WebApplicationException(Response.status(404)
                                .entity("Username already exists")
                                .build());
                    });
        }
    }
}
