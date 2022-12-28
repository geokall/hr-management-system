package repository;

import entity.HuaCompensation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HuaCompensationRepository extends JpaRepository<HuaCompensation, Long> {

    List<HuaCompensationRepository> findByUserId(Long userId);

}
