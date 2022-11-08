package service;

import dto.BasicInformationDTO;
import dto.UserDTO;
import entity.HuaRole;
import entity.HuaUser;
import exception.HuaConflictException;
import exception.HuaNotFoundException;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import org.springframework.util.ObjectUtils;
import repository.HuaRoleRepository;
import repository.HuaUserRepository;
import utils.HuaUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.Date;

import static utils.StaticRole.READER_ROLE;

@ApplicationScoped
public class UserServiceImpl implements UserService {

    private final HuaUserRepository huaUserRepository;
    private final HuaRoleRepository huaRoleRepository;
    private final Mailer mailer;

    @Inject
    public UserServiceImpl(HuaUserRepository huaUserRepository,
                           HuaRoleRepository huaRoleRepository,
                           Mailer mailer) {
        this.huaUserRepository = huaUserRepository;
        this.huaRoleRepository = huaRoleRepository;
        this.mailer = mailer;
    }

    @Override
    public UserDTO findUserInfo(Long id) {
        HuaUser user = findUserBy(id);

        return toUserDTO(user);
    }

    @Override
    public void updateUserInfo(Long id, UserDTO dto) {
        HuaUser user = findUserBy(id);

        updateUserInfoBy(user);

        huaUserRepository.save(user);
    }

    @Override
    public void inviteUser(String email) {
        findExistingUserBy(email);

        HuaUser huaUser = new HuaUser();

        String tempPassword = HuaUtil.generateRandomPasswordBy();

        String hashedPassword = BcryptUtil.bcryptHash(tempPassword);
        huaUser.setPassword(hashedPassword);

        String username = HuaUtil.generateUsernameBy(email);
        huaUser.setUsername(username);

        huaUser.setEmail(email);
        huaUser.setDateCreated(LocalDateTime.now());

        huaRoleRepository.findByName(READER_ROLE)
                .ifPresent(huaUser::addRole);

        huaUserRepository.save(huaUser);

        sendInvitation(email, huaUser, tempPassword);
    }


    private UserDTO toUserDTO(HuaUser user) {
        UserDTO dto = new UserDTO();

        BasicInformationDTO basicInformationDTO = setBasicInformation(user);

        dto.setBasicInformation(basicInformationDTO);

        return dto;
    }

    private BasicInformationDTO setBasicInformation(HuaUser user) {
        BasicInformationDTO dto = new BasicInformationDTO();

        dto.setId(user.getId());

        String role = user.getRoles().stream()
                .findFirst()
                .map(HuaRole::getName)
                .orElse(null);

        dto.setRole(role);

        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setSurname(user.getSurname());
        dto.setCreatedDate(user.getDateCreated());
        dto.setUsername(user.getUsername());
        dto.setMobileNumber(user.getMobileNumber());
        dto.setVatNumber(user.getVatNumber());

        if (user.getBirthDate() != null) {
            String birthDateFormatted = HuaUtil.formatDateToString(user.getBirthDate());
            dto.setBirthDate(birthDateFormatted);
        }

        if (user.getHireDate() != null) {
            String hireDateFormatted = HuaUtil.formatDateToString(user.getHireDate());
            dto.setHireDate(hireDateFormatted);
        }

        dto.setGender(user.getGender() != null ? user.getGender().getLabel() : null);
        dto.setEmployeeStatus(user.getEmployeeStatus() != null ? user.getEmployeeStatus().getLabel() : null);
        dto.setJobStatus(user.getJobStatus() != null ? user.getJobStatus().getLabel() : null);
        dto.setMaritalStatus(user.getMaritalStatus() != null ? user.getMaritalStatus().getLabel() : null);

        return dto;
    }

    private void updateUserInfoBy(HuaUser user) {
        BasicInformationDTO dto = new BasicInformationDTO();

        user.setSurname(dto.getSurname());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setMobileNumber(dto.getMobileNumber());
        user.setVatNumber(dto.getVatNumber());

        String birthDate = dto.getBirthDate();

        if (!ObjectUtils.isEmpty(birthDate)) {
            Date birthDateFormatted = HuaUtil.formatStringToDate(birthDate);
            user.setBirthDate(birthDateFormatted);
        }

        user.setLastModificationDate(LocalDateTime.now());
    }

    private HuaUser findUserBy(Long id) {
        return huaUserRepository.findById(id)
                .orElseThrow(() -> new HuaNotFoundException("Ο χρήστης δεν βρέθηκε."));
    }

    private void findExistingUserBy(String email) {
        huaUserRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new HuaConflictException("Ο χρήστης υπάρχει ήδη");
                });
    }

    private void sendInvitation(String email, HuaUser huaUser, String tempPassword) {
        mailer.send(Mail.withText(email,
                        "Invitation for HUA Management System",
                        "Username: " + huaUser.getUsername() +
                                "\n" +
                                "Password: " + tempPassword
                )
        );
    }
}