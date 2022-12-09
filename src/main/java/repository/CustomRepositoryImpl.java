package repository;

import dto.DirectReportDTO;
import dto.ManagerDTO;
import org.hibernate.query.NativeQuery;
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
    EntityManager entityManager;

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
    public ManagerDTO findUserReportingManger(Long userId) {
        //pending job title
        return (ManagerDTO) entityManager.createNativeQuery("SELECT hu.name, hu.surname " +
                        "FROM {h-schema}hua_user hu " +
                        "INNER JOIN {h-schema}user_managers um ON hu.id = um.manager_id " +
                        "INNER JOIN {h-schema}hua_manager hm ON hu.id = hm.user_id " +
                        "where um.user_id = :id")
                .setParameter("id", userId)
                .unwrap(NativeQuery.class)
                .setResultTransformer(Transformers.aliasToBean(ManagerDTO.class))
                .getSingleResult();
    }

    @Override
    public List<DirectReportDTO> findUserDirectReports(Long userId) {
        return entityManager.createNativeQuery("SELECT hu.name, hu.surname " +
                        "FROM {h-schema}hua_user hu " +
                        "INNER JOIN {h-schema}user_direct_reports um ON hu.id = um.direct_report_id " +
                        "INNER JOIN {h-schema}hua_direct_report hm ON hu.id = hm.user_id " +
                        "where um.user_id = :id")
                .setParameter("id", userId)
                .unwrap(NativeQuery.class)
                .setResultTransformer(Transformers.aliasToBean(DirectReportDTO.class))
                .getResultList();
    }
}
