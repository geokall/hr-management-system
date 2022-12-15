package service;

import dto.IdNameDTO;
import repository.HuaDivisionRepository;
import repository.HuaLocationRepository;
import repository.HuaUserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class DataServiceImpl implements DataService {

    private final HuaLocationRepository locationRepository;
    private final HuaDivisionRepository divisionRepository;
    private final HuaUserRepository userRepository;

    @Inject
    public DataServiceImpl(HuaLocationRepository locationRepository,
                           HuaDivisionRepository divisionRepository,
                           HuaUserRepository userRepository) {
        this.locationRepository = locationRepository;
        this.divisionRepository = divisionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<IdNameDTO> fetchLocations() {
        return locationRepository.findAll().stream()
                .map(entity -> new IdNameDTO(entity.getId(), entity.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<IdNameDTO> fetchDivisions() {
        return divisionRepository.findAll().stream()
                .map(entity -> new IdNameDTO(entity.getId(), entity.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<IdNameDTO> fetchUsers() {
        return userRepository.findAll().stream()
                .map(entity -> new IdNameDTO(entity.getId(), entity.getName()))
                .collect(Collectors.toList());
    }

}
