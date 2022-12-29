package repository;

import entity.HuaWorkInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HuaWorkInformationRepository extends JpaRepository<HuaWorkInformation, Long> {

    List<HuaWorkInformation> findByUserId(Long userId);

    List<HuaWorkInformation> findByManagerId(Long managerId);

    Optional<HuaWorkInformation> findFirstByUserIdOrderByEffectiveDateDesc(Long userId);

}
