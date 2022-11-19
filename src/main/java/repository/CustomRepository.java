package repository;

import dto.DirectReportDTO;
import dto.ManagerDTO;

import java.util.List;

public interface CustomRepository {

    List<String> findUserRole(Long id);

    ManagerDTO findUserReportingManger(Long userId);

    List<DirectReportDTO> findUserDirectReports(Long userId);
}
