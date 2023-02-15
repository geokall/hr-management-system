package service;

import dto.*;
import entity.HuaUser;
import entity.HuaWorkInformation;
import exception.HuaConflictException;
import exception.HuaNotFoundException;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import repository.HuaRoleRepository;
import repository.HuaUserRepository;
import repository.HuaWorkInformationRepository;
import utils.HuaUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static exception.HuaCommonError.*;
import static utils.HuaUtil.*;
import static utils.StaticRole.READER_ROLE;

@ApplicationScoped
public class UserServiceImpl implements UserService {

    @ConfigProperty(name = "notification.bucket.link")
    String quarkusBucketLink;

    @ConfigProperty(name = "invitation.hua.link")
    String managementSystemLink;

    private final Mailer mailer;
    private final HuaUserRepository huaUserRepository;
    private final HuaRoleRepository huaRoleRepository;
    private final HuaWorkInformationRepository workInformationRepository;
    private final MinioService minioService;

    @Inject
    public UserServiceImpl(Mailer mailer,
                           HuaUserRepository huaUserRepository,
                           HuaRoleRepository huaRoleRepository,
                           HuaWorkInformationRepository workInformationRepository,
                           MinioService minioService) {
        this.mailer = mailer;
        this.huaUserRepository = huaUserRepository;
        this.huaRoleRepository = huaRoleRepository;
        this.workInformationRepository = workInformationRepository;
        this.minioService = minioService;
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

        LocalDate hireDate = toLocalDateBy(dto.getHireDate());

        if (hireDate != null) {
            Period between = Period.between(hireDate, LocalDate.now());
            dto.setCountYears(between.getYears());
            dto.setCountMonths(between.getMonths());
            dto.setCountDays(between.getDays());
        }

        dto.setEmployeeNumber(user.getEmployeeNumber());

        //order by must not be null
        //fetching all by user id, and then sets the latest update by effective date
        ManagerDTO directManager = workInformationRepository.findByUserId(id).stream()
                .map(workInformation -> workInformationRepository.findFirstByUserIdOrderByEffectiveDateDesc(id)
                        .map(huaWorkInformation -> toDirectManagerDTO(huaWorkInformation, dto))
                        .orElse(new ManagerDTO()))
                .findFirst()
                .orElse(new ManagerDTO());

        dto.setDirectManager(directManager);

        Set<DirectReportDTO> directReports = workInformationRepository.findByManagerId(id).stream()
                .map(this::toDirectReportDTO)
                .collect(Collectors.toSet());

        dto.setDirectReports(directReports);

        BooleanOnlyDTO isBucketExist = minioService.isBucketExistBy(user.getUsername());
        dto.setIsBucketExist(isBucketExist.isExist());

        return dto;
    }

    @Override
    public void inviteUser(String email, UriInfo uriInfo) {
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

        HuaUser saved = huaUserRepository.save(huaUser);

        huaUser.setEmployeeNumber(saved.getId());

        huaUserRepository.save(huaUser);

        sendInvitation(email, huaUser, tempPassword, uriInfo);
    }

    @Override
    public void inviteManager(String username, Long managerId) {
        if (managerId != null) {
            HuaUser manager = findUserBy(managerId);

            HuaUser loggedInUser = findUserByUsername(username);

            String businessEmail = manager.getBusinessEmail();

            notifyManagerBy(businessEmail, loggedInUser);
        }
    }

    @Override
    public void changeUserPassword(Long id, PasswordDTO dto) {
        HuaUser user = findUserBy(id);

        validatePassword(dto, user.getPassword());

        String hashedPassword = BcryptUtil.bcryptHash(dto.getNewPassword());
        user.setPassword(hashedPassword);

        user.setLastModificationDate(LocalDateTime.now());

        huaUserRepository.save(user);
    }


    private HuaUser findUserBy(Long id) {
        return huaUserRepository.findById(id)
                .orElseThrow(() -> new HuaNotFoundException(USER_NOT_FOUND));
    }

    private void findExistingUserBy(String email) {
        huaUserRepository.findByBusinessEmail(email)
                .ifPresent(user -> {
                    throw new HuaConflictException(USER_ALREADY_EXIST);
                });
    }

    private HuaUser findUserByUsername(String username) {
        return huaUserRepository.findByUsername(username)
                .orElseThrow(() -> new HuaNotFoundException(USER_NOT_FOUND));
    }

    private void sendInvitation(String email, HuaUser huaUser, String tempPassword, UriInfo uriInfo) {
        String username = huaUser.getUsername();

        String boldUsername = OPEN_BOLD.concat(username).concat(CLOSE_BOLD);
        String boldPassword = OPEN_BOLD.concat(tempPassword).concat(CLOSE_BOLD);

        URI requestUri = uriInfo.getRequestUri();
        String host = requestUri.getHost();

        String invitationUrl;

        invitationUrl = retrieveInvitationUrl(host);

        mailer.send(Mail.withHtml(email,
                "You have been invited to use HUA management system",
                "Your temporary credentials to sign in:" +
                        BREAK_LINE +
                        BREAK_LINE +
                        "Username: " + boldUsername +
                        BREAK_LINE +
                        "Password: " + boldPassword +
                        BREAK_LINE +
                        BREAK_LINE +
                        "You can change your credentials in the app." +
                        BREAK_LINE +
                        BREAK_LINE +
                        "Please use the link below:" +
                        BREAK_LINE +
                        OPEN_LINK.concat(invitationUrl).concat(">") +
                        "HR management system" +
                        CLOSE_LINK
                )
        );
    }

    private void notifyManagerBy(String managerEmail, HuaUser loggedInUser) {
        String username = loggedInUser.getUsername();

        String boldUsername = OPEN_BOLD.concat(username).concat(CLOSE_BOLD);

        mailer.send(Mail.withHtml(managerEmail,
                        "Minio bucket update",
                        "Username: " +
                                boldUsername +
                                " updated its Minio bucket." +
                                BREAK_LINE +
                                BREAK_LINE +
                                "To check the updated bucket, please visit the link below: " +
                                BREAK_LINE +
                                quarkusBucketLink +
                                username
                )
        );
    }

    private ManagerDTO toDirectManagerDTO(HuaWorkInformation workInformation, MainInfoDTO dto) {
        ManagerDTO managerDTO = new ManagerDTO();
        managerDTO.setId(workInformation.getId());

        Optional.ofNullable(workInformation.getManager())
                .ifPresent(manager -> {
                    managerDTO.setManagerId(manager.getId());
                    managerDTO.setName(manager.getName());
                    managerDTO.setSurname(manager.getSurname());

                    workInformationRepository.findByUserId(manager.getId()).stream()
                            .findFirst()
                            .ifPresent(managerInfo -> managerDTO.setTitleJob(managerInfo.getJobTitle()));
                });

        dto.setDivision(workInformation.getDivision() != null ? workInformation.getDivision().getName() : null);
        dto.setLocation(workInformation.getLocation() != null ? workInformation.getLocation().getName() : null);

        dto.setJobTitle(workInformation.getJobTitle());
        dto.setJobStatus(workInformation.getJobStatus() != null ? workInformation.getJobStatus().name() : null);

        return managerDTO;
    }

    private DirectReportDTO toDirectReportDTO(HuaWorkInformation workInformation) {
        DirectReportDTO directReportDTO = new DirectReportDTO();
        directReportDTO.setId(workInformation.getId());

        Optional.ofNullable(workInformation.getUser())
                .ifPresent(user -> {
                    directReportDTO.setDirectReportUserId(user.getId());
                    directReportDTO.setName(user.getName());
                    directReportDTO.setSurname(user.getSurname());
                });

        return directReportDTO;
    }

    private void validatePassword(PasswordDTO dto, String password) {
        String plainTextCurrentPassword = dto.getCurrentPassword();

        if (!BcryptUtil.matches(plainTextCurrentPassword, password)) {
            throw new HuaConflictException(USER_CURRENT_PASSWORD_NOT_EQUALS);
        }

        if (!dto.getNewPassword().equals(dto.getSameNewPassword())) {
            throw new HuaConflictException(USER_NEW_PASSWORD_NOT_EQUALS);
        }
    }

    private String retrieveInvitationUrl(String host) {
        String invitationUrl;
        if (host.contains("localhost")) {
            invitationUrl = "http://".concat(host)
                    .concat(":")
                    .concat("4200")
                    .concat("/login");
        } else {
            invitationUrl = managementSystemLink;
        }

        return invitationUrl;
    }
}
