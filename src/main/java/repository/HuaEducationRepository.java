package repository;

import entity.HuaEducation;
import entity.HuaUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HuaEducationRepository extends JpaRepository<HuaEducation, Long> {

    List<HuaEducation> findByUser(HuaUser user);
}
