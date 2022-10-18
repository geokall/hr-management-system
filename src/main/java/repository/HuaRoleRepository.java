package repository;

import entity.HuaRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HuaRoleRepository extends JpaRepository<HuaRole, Long>, CustomRepository {

    Optional<HuaRole> findByName(String name);
}
