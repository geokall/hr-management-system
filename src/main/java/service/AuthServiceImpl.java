package service;

import dto.LoginDTO;
import dto.RegisterDTO;
import entity.HuaUser;
import exception.HuaConflictException;
import exception.HuaNotFoundException;
import io.quarkus.elytron.security.common.BcryptUtil;
import org.springframework.util.ObjectUtils;
import repository.HuaRoleRepository;
import repository.HuaUserRepository;
import security.JwtClaimService;
import security.JwtResponseDTO;
import utils.HuaUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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
                .email(huaUser.getBusinessEmail())
                .username(huaUser.getUsername())
                .token(jwt)
                .role(userRole)
                .build();
    }

    @Override
    public Long register(String role, RegisterDTO dto) {
        handleDuplicates(dto);

        HuaUser huaUser = new HuaUser();
        huaUser.setSurname(dto.getSurname());
        huaUser.setName(dto.getName());

        String username = dto.getUsername();
        String email = HuaUtil.generateEmailBy(username);

        huaUser.setUsername(username);
        huaUser.setBusinessEmail(email);

        String hashedPassword = BcryptUtil.bcryptHash(dto.getPassword());
        huaUser.setPassword(hashedPassword);

        huaUser.setDateCreated(LocalDateTime.now());

        huaRoleRepository.findByName(role)
                .ifPresent(huaUser::addRole);

        HuaUser savedUser = huaUserRepository.save(huaUser);

        return savedUser.getId();
    }


    private void checkHashedPassword(HuaUser huaUser, LoginDTO dto) {
        String hashedPassword = huaUser.getPassword();
        String password = dto.getPassword();

        boolean matches = BcryptUtil.matches(password, hashedPassword);

        if (!matches) {
            throw new HuaNotFoundException("Λάθος στοιχεία.");
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
                    throw new HuaNotFoundException("Λάθος στοιχεία.");
                });
    }

    private void handleDuplicates(RegisterDTO dto) {
        if (!ObjectUtils.isEmpty(dto.getBusinessEmail())) {
            huaUserRepository.findByBusinessEmail(dto.getBusinessEmail())
                    .ifPresent(user -> {
                        throw new HuaConflictException("To email χρησιμοποιείται ήδη.");
                    });
        }

        if (!ObjectUtils.isEmpty(dto.getUsername())) {
            huaUserRepository.findByUsername(dto.getUsername())
                    .ifPresent(user -> {
                        throw new HuaConflictException("To username χρησιμοποιείται ήδη.");
                    });
        }
    }
}
