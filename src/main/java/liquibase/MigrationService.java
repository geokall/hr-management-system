//package liquibase;
//
//import io.quarkus.liquibase.LiquibaseFactory;
//import liquibase.changelog.ChangeSetStatus;
//import liquibase.exception.LiquibaseException;
//
//import javax.enterprise.context.ApplicationScoped;
//import javax.inject.Inject;
//import java.util.List;
//
//@ApplicationScoped
//public class MigrationService {
//
//    private final LiquibaseFactory liquibaseFactory;
//
//    @Inject
//    public MigrationService(LiquibaseFactory liquibaseFactory) {
//        this.liquibaseFactory = liquibaseFactory;
//    }
//
//    public void checkMigration() throws LiquibaseException {
//        // Get the list of liquibase change set statuses
//        try (Liquibase liquibase = liquibaseFactory.createLiquibase()) {
//            liquibase.getChangeSetStatuses(liquibaseFactory.createContexts(), liquibaseFactory.createLabels());
//        }
//    }
//}
