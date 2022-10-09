package repository;

import entity.HuaUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HuaUserRepository extends JpaRepository<HuaUser, Long> {
}
