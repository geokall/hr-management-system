package repository;

import dto.DirectReportDTO;
import dto.ManagerDTO;

import java.util.List;

public interface CustomRepository {

    List<String> findUserRole(Long id);

    List<ManagerDTO> findUserReportingManger(Long userId);

    List<DirectReportDTO> findUserDirectReports(Long userId);
}
