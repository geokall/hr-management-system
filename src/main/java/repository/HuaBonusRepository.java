package repository;

import entity.HuaBonus;
import entity.HuaUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HuaBonusRepository extends JpaRepository<HuaBonus, Long> {

    Optional<HuaBonus> findByUser(HuaUser user);
}
