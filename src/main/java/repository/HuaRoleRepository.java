package repository;

import entity.HuaRole;
import entity.HuaUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface HuaRoleRepository extends JpaRepository<HuaRole, Long>, CustomRepository {

    Optional<HuaRole> findByName(String name);
}
