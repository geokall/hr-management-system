package service;

import dto.UserDTO;

import java.util.List;

public interface SearchService {

    List<UserDTO> findAllUsers();
}
