package repository;

import entity.HuaUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HuaUserRepository extends JpaRepository<HuaUser, Long> {

    Optional<HuaUser> findByUsernameAndPassword(String username, String password);

    Optional<HuaUser> findByUsername(String username);

    Optional<HuaUser> findByBusinessEmail(String email);
}
