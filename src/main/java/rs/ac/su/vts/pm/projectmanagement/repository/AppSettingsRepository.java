package rs.ac.su.vts.pm.projectmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import rs.ac.su.vts.pm.projectmanagement.model.entity.AppSettings;

@Repository
public interface AppSettingsRepository
        extends JpaRepository<AppSettings, Long>, QuerydslPredicateExecutor<AppSettings>
{

}
