package repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class CustomRepositoryImpl implements CustomRepository {

    //Implemented custom repo with EM, since Quarkus - JPA not support native query.
    //The typical way would be: List<HuaRole> findByRoles_Id(Long id);
    //But it gives the below quarkus exception:
    //Offending method is 'findByRoles_Id' of repository 'repository.HuaRoleRepository'.

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<String> findUserRole(Long id) {
        //business will have only one role
        return entityManager.createNativeQuery("SELECT hr.name from management.HUA_USER hu " +
                        "INNER JOIN management.USER_ROLE ur on hu.id = ur.user_id " +
                        "INNER JOIN management.HUA_ROLE hr on ur.role_id = hr.id " +
                        "WHERE hu.id = :id")
                .setParameter("id", id)
                .getResultList();
    }
}
