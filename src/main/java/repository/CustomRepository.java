package repository;

import java.util.List;

public interface CustomRepository {

    List<String> findUserRole(Long id);
}
