package repository;

import dto.IdNameProjectionDTO;
import org.hibernate.transform.Transformers;

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
        return entityManager.createNativeQuery("SELECT hr.name from {h-schema}HUA_USER hu " +
                        "INNER JOIN {h-schema}USER_ROLE ur ON hu.id = ur.user_id " +
                        "INNER JOIN {h-schema}HUA_ROLE hr ON ur.role_id = hr.id " +
                        "WHERE hu.id = :id")
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<IdNameProjectionDTO> findAvailableManagersToReport(Long id) {
        return entityManager.createNativeQuery("SELECT u.id as id, concat(u.name, ' ', u.surname) as name FROM {h-schema}hua_user u " +
                        "WHERE u.id NOT IN (SELECT w.user_id FROM {h-schema}hua_work_information w " +
                        "WHERE w.manager_id = :loggedInUserId) " +
                        "AND u.id <> :loggedInUserId")
                .setParameter("loggedInUserId", id)
                .unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(Transformers.aliasToBean(IdNameProjectionDTO.class))
                .getResultList();
    }
}
