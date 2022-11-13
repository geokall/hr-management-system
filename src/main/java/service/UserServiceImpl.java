package service;

import dto.BasicInformationDTO;
import dto.PersonalInfoDTO;
import entity.HuaUser;
import enums.EmployeeStatusEnum;
import enums.GenderEnum;
import enums.MaritalStatusEnum;
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
    public PersonalInfoDTO findPersonalInfo(Long id) {
        HuaUser user = findUserBy(id);

        return toUserDTO(user);
    }

    @Override
    public void updateBasicInformation(Long id, PersonalInfoDTO dto) {
        HuaUser user = findUserBy(id);

        updateBasicInformationBy(user);

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

        huaUser.setBusinessEmail(email);
        huaUser.setDateCreated(LocalDateTime.now());

        huaRoleRepository.findByName(READER_ROLE)
                .ifPresent(huaUser::addRole);

        huaUserRepository.save(huaUser);

        sendInvitation(email, huaUser, tempPassword);
    }


    private PersonalInfoDTO toUserDTO(HuaUser user) {
        PersonalInfoDTO dto = new PersonalInfoDTO();

        BasicInformationDTO basicInformationDTO = setBasicInformation(user);

        dto.setBasicInformation(basicInformationDTO);

        return dto;
    }

    private BasicInformationDTO setBasicInformation(HuaUser user) {
        BasicInformationDTO dto = new BasicInformationDTO();

        dto.setId(user.getId());

        dto.setName(user.getName());
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

        dto.setBusinessEmail(user.getBusinessEmail());
        dto.setPersonalEmail(user.getPersonalEmail());

        dto.setGender(user.getGender() != null ? user.getGender().name() : null);
        dto.setEmployeeStatus(user.getEmployeeStatus() != null ? user.getEmployeeStatus().name() : null);
        dto.setMaritalStatus(user.getMaritalStatus() != null ? user.getMaritalStatus().name() : null);

        return dto;
    }

    private void updateBasicInformationBy(HuaUser user) {
        BasicInformationDTO dto = new BasicInformationDTO();

        //basic
        user.setSurname(dto.getSurname());
        user.setName(dto.getName());
        user.setUsername(dto.getUsername());
        user.setVatNumber(dto.getVatNumber());
        user.setGender(GenderEnum.valueOf(dto.getGender()));
        user.setMaritalStatus(MaritalStatusEnum.valueOf(dto.getMaritalStatus()));
        user.setEmployeeStatus(EmployeeStatusEnum.valueOf(dto.getEmployeeStatus()));

        String birthDate = dto.getBirthDate();

        if (!ObjectUtils.isEmpty(birthDate)) {
            Date birthDateFormatted = HuaUtil.formatStringToDate(birthDate);
            user.setBirthDate(birthDateFormatted);
        }

        //address
        user.setStreet1(dto.getStreet1());
        user.setStreet2(dto.getStreet2());
        user.setCity(dto.getCity());
        user.setProvince(dto.getProvince());
        user.setPostalCode(dto.getPostalCode());
        user.setCountry(dto.getCountry());

        //contact
        user.setMobileNumber(dto.getMobileNumber());
        user.setWorkNumber(dto.getWorkNumber());
        user.setHomeNumber(dto.getHomeNumber());
        user.setBusinessEmail(dto.getBusinessEmail());
        user.setPersonalEmail(dto.getPersonalEmail());

        //social
        user.setLinkedinUrl(dto.getLinkedinUrl());
        user.setTwitterUrl(dto.getTwitterUrl());
        user.setFacebookUrl(dto.getFacebookUrl());

        //education


        user.setLastModificationDate(LocalDateTime.now());
    }

    private HuaUser findUserBy(Long id) {
        return huaUserRepository.findById(id)
                .orElseThrow(() -> new HuaNotFoundException("Ο χρήστης δεν βρέθηκε."));
    }

    private void findExistingUserBy(String email) {
        huaUserRepository.findByBusinessEmail(email)
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
