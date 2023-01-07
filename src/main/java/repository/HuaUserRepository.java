package repository;

import entity.HuaUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HuaUserRepository extends JpaRepository<HuaUser, Long>, CustomRepository {

    Optional<HuaUser> findByUsernameAndPassword(String username, String password);

    Optional<HuaUser> findByUsername(String username);

    Optional<HuaUser> findByBusinessEmail(String email);

    List<HuaUser> findAllByUsernameIsNot(String username);
}
