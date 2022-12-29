package service;

import dto.DivisionDTO;
import dto.LocationDTO;
import dto.UserDTO;
import entity.HuaDivision;
import entity.HuaLocation;
import entity.HuaUser;
import repository.HuaUserRepository;
import repository.HuaWorkInformationRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class SearchServiceImpl implements SearchService {

    private final HuaUserRepository userRepository;
    private final HuaWorkInformationRepository workInformationRepository;

    @Inject
    public SearchServiceImpl(HuaUserRepository userRepository,
                             HuaWorkInformationRepository workInformationRepository) {
        this.userRepository = userRepository;
        this.workInformationRepository = workInformationRepository;
    }

    @Override
    public List<UserDTO> findAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toUserDTO)
                .collect(Collectors.toList());
    }


    private UserDTO toUserDTO(HuaUser user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setSurname(user.getSurname());
        userDTO.setBusinessEmail(user.getBusinessEmail());
        userDTO.setLinkedinUrl(user.getLinkedinUrl());
        userDTO.setUsername(user.getUsername());
        userDTO.setWorkNumber(user.getWorkNumber());

        workInformationRepository.findByUserId(user.getId())
                .forEach(workInformation -> workInformationRepository.findFirstByUserIdOrderByEffectiveDateDesc(user.getId())
                        .ifPresent(huaWorkInformation -> {
                            DivisionDTO divisionDTO = Optional.ofNullable(huaWorkInformation.getDivision())
                                    .map(this::toDivisionDTO)
                                    .orElse(new DivisionDTO());

                            LocationDTO locationDTO = Optional.ofNullable(huaWorkInformation.getLocation())
                                    .map(this::toLocationDTO)
                                    .orElse(new LocationDTO());

                            userDTO.setDivision(divisionDTO);
                            userDTO.setLocation(locationDTO);
                        }));

        return userDTO;
    }


    private DivisionDTO toDivisionDTO(HuaDivision huaDivision) {
        DivisionDTO divisionDTO = new DivisionDTO();
        divisionDTO.setId(huaDivision.getId());
        divisionDTO.setName(huaDivision.getName());

        return divisionDTO;
    }

    private LocationDTO toLocationDTO(HuaLocation huaLocation) {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setId(huaLocation.getId());
        locationDTO.setName(huaLocation.getName());

        return locationDTO;
    }
}
