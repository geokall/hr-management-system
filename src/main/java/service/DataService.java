package service;

import dto.IdNameDTO;

import java.util.List;

public interface DataService {

    List<IdNameDTO> fetchLocations();

    List<IdNameDTO> fetchDivisions();

    List<IdNameDTO> fetchUsers(String loginName);
}
