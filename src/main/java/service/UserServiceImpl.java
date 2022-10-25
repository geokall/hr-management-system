package service;

import dto.UserDTO;
import entity.HuaUser;
import exception.HuaNotFoundException;
import repository.HuaUserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

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


    private UserDTO toUserDTO(HuaUser user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setSurname(user.getSurname());
        dto.setBirthDate(user.getBirthDate());
        dto.setCreatedDate(user.getDateCreated());
        dto.setUsername(user.getUsername());

        return dto;
    }
}
