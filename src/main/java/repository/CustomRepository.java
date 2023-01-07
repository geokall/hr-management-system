package repository;

import dto.IdNameProjectionDTO;

import java.util.List;

public interface CustomRepository {

    List<String> findUserRole(Long id);

    List<IdNameProjectionDTO> findAvailableManagersToReport(Long id);
}
