package repository;

import dto.ManagerDTO;

import java.util.List;

public interface CustomRepository {

    List<String> findUserRole(Long id);

    ManagerDTO findUserReportingManger(Long userId);
}
