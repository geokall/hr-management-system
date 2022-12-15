package repository;

import entity.HuaWorkInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HuaWorkInformationRepository extends JpaRepository<HuaWorkInformation, Long> {

    List<HuaWorkInformation> findByUserId(Long userId);
}
