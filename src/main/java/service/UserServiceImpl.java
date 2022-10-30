package service;

import dto.UserDTO;
import entity.HuaRole;
import entity.HuaUser;
import exception.HuaNotFoundException;
import org.springframework.util.ObjectUtils;
import repository.HuaUserRepository;
import utils.HuaUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Date;
import java.util.Set;

@ApplicationScoped
public class UserServiceImpl implements UserService {

    private final HuaUserRepository huaUserRepository;

    @Inject
    public UserServiceImpl(HuaUserRepository huaUserRepository) {
        this.huaUserRepository = huaUserRepository;
    }

    @Override
    public UserDTO findUserInfo(Long id) {
        HuaUser user = huaUserRepository.findById(id)
                .orElseThrow(() -> new HuaNotFoundException("User not found"));

        return toUserDTO(user);
    }

    @Override
    public void updateUserInfo(Long id, UserDTO dto) {
        HuaUser user = huaUserRepository.findById(id)
                .orElseThrow(() -> new HuaNotFoundException("User not found"));

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

        huaUserRepository.save(user);
    }


    private UserDTO toUserDTO(HuaUser user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
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

        String role = user.getRoles().stream()
                .findFirst()
                .map(HuaRole::getName)
                .orElse(null);

        dto.setRole(role);

        return dto;
    }
}
