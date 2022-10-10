package repository;

import entity.HuaRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HuaRoleRepository extends JpaRepository<HuaRole, Long> {

//    List<HuaRole> findByRoles_Id(Long id);

    Optional<HuaRole> findByName(String name);
}
