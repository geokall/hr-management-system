package repository;

import entity.HuaRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HuaRoleRepository extends JpaRepository<HuaRole, Long>, CustomRepository {
}
