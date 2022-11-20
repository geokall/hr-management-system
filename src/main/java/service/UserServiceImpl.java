package service;

import dto.*;
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
import java.util.List;

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
    public InformationDTO findPersonalInfo(Long id) {
        HuaUser user = findUserBy(id);

        return toUserDTO(user);
    }

    @Override
    public MainInfoDTO findMainInfo(Long id) {
        HuaUser user = findUserBy(id);

        MainInfoDTO dto = new MainInfoDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());

        dto.setWorkNumber(user.getWorkNumber());
        dto.setMobileNumber(user.getMobileNumber());
        dto.setBusinessEmail(user.getBusinessEmail());
        dto.setHireDate(user.getHireDate());

        dto.setEmployeeNumber(user.getEmployeeNumber());
        dto.setJobStatus(user.getJobStatus() != null ? user.getJobStatus().getLabel() : null);

        dto.setDivision(user.getDivision() != null ? user.getDivision().getName() : null);
        dto.setLocation(user.getLocation() != null ? user.getLocation().getName() : null);

        ManagerDTO directManager = huaRoleRepository.findUserReportingManger(id);

        dto.setDirectManager(directManager);

        List<DirectReportDTO> directReports = huaRoleRepository.findUserDirectReports(id);

        dto.setDirectReports(directReports);

        return dto;
    }

    @Override
    public void updateBasicInformation(Long id, InformationDTO dto) {
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


    private InformationDTO toUserDTO(HuaUser user) {
        InformationDTO dto = new InformationDTO();

        PersonalInformationDTO personalInformationDTO = setBasicInformation(user);

        dto.setPersonalInformation(personalInformationDTO);

        return dto;
    }

    private PersonalInformationDTO setBasicInformation(HuaUser user) {
        PersonalInformationDTO dto = new PersonalInformationDTO();

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
        PersonalInformationDTO dto = new PersonalInformationDTO();

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
