package service;

import dto.DirectReportDTO;
import dto.MainInfoDTO;
import dto.ManagerDTO;
import entity.HuaUser;
import entity.HuaWorkInformation;
import exception.HuaConflictException;
import exception.HuaNotFoundException;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import repository.HuaRoleRepository;
import repository.HuaUserRepository;
import repository.HuaWorkInformationRepository;
import utils.HuaUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static exception.HuaCommonError.USER_ALREADY_EXIST;
import static exception.HuaCommonError.USER_NOT_FOUND;
import static utils.StaticRole.READER_ROLE;

@ApplicationScoped
public class UserServiceImpl implements UserService {

    private final Mailer mailer;
    private final HuaUserRepository huaUserRepository;
    private final HuaRoleRepository huaRoleRepository;
    private final HuaWorkInformationRepository workInformationRepository;

    @Inject
    public UserServiceImpl(Mailer mailer,
                           HuaUserRepository huaUserRepository,
                           HuaRoleRepository huaRoleRepository,
                           HuaWorkInformationRepository workInformationRepository) {
        this.mailer = mailer;
        this.huaUserRepository = huaUserRepository;
        this.huaRoleRepository = huaRoleRepository;
        this.workInformationRepository = workInformationRepository;
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

        //supporting only 1 atm
        ManagerDTO directManager = workInformationRepository.findTopByUserIdOrderByEffectiveDateDesc(id)
                .map(this::toDirectManagerDTO)
                .orElse(null);

        dto.setDirectManager(directManager);

        List<DirectReportDTO> directReports = workInformationRepository.findByManagerId(id).stream()
                .map(this::toDirectReportDTO)
                .collect(Collectors.toList());

        dto.setDirectReports(directReports);

        return dto;
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

    private void sendInvitation(String email, HuaUser huaUser, String tempPassword) {
        mailer.send(Mail.withText(email,
                        "Invitation for HUA Management System",
                        "Username: " + huaUser.getUsername() +
                                "\n" +
                                "Password: " + tempPassword
                )
        );
    }

    private ManagerDTO toDirectManagerDTO(HuaWorkInformation workInformation) {
        ManagerDTO managerDTO = new ManagerDTO();
        managerDTO.setId(workInformation.getId());

        Optional.ofNullable(workInformation.getManager())
                .ifPresent(manager -> {
                    managerDTO.setName(manager.getName());
                    managerDTO.setSurname(manager.getSurname());

                    workInformationRepository.findByUserId(manager.getId()).stream()
                            .findFirst()
                            .ifPresent(managerInfo -> managerDTO.setTitleJob(managerInfo.getJobTitle()));
                });

        return managerDTO;
    }

    private DirectReportDTO toDirectReportDTO(HuaWorkInformation workInformation) {
        DirectReportDTO directReportDTO = new DirectReportDTO();
        directReportDTO.setId(workInformation.getId());

        Optional.ofNullable(workInformation.getUser())
                .ifPresent(user -> {
                    directReportDTO.setName(user.getName());
                    directReportDTO.setSurname(user.getSurname());
                });

        return directReportDTO;
    }
}
