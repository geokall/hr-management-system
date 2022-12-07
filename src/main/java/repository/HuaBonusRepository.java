package repository;

import entity.HuaBonus;
import entity.HuaUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HuaBonusRepository extends JpaRepository<HuaBonus, Long> {

    List<HuaBonus> findByUser(HuaUser user);
}
