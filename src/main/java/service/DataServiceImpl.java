package service;

import dto.IdNameDTO;
import repository.HuaDivisionRepository;
import repository.HuaLocationRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class DataServiceImpl implements DataService {

    private final HuaLocationRepository locationRepository;
    private final HuaDivisionRepository divisionRepository;

    @Inject
    public DataServiceImpl(HuaLocationRepository locationRepository,
                           HuaDivisionRepository divisionRepository) {
        this.locationRepository = locationRepository;
        this.divisionRepository = divisionRepository;
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

}
