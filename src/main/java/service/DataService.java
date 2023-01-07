package service;

import dto.IdNameDTO;
import dto.IdNameProjectionDTO;

import java.util.List;

public interface DataService {

    List<IdNameDTO> fetchLocations();

    List<IdNameDTO> fetchDivisions();

    List<IdNameProjectionDTO> fetchUsers(String loginName);
}
