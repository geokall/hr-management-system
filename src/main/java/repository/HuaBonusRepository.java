package repository;

import entity.HuaBonus;
import entity.HuaUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HuaBonusRepository extends JpaRepository<HuaBonus, Long> {

    Optional<HuaBonus> findByUser(HuaUser user);
}
