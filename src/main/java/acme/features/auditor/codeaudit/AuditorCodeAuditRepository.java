
package acme.features.auditor.codeaudit;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.project.Project;
import acme.roles.Manager;

@Repository
public interface AuditorCodeAuditRepository extends AbstractRepository {

	@Query("SELECT a FROM Auditor a WHERE a.userAccount.id = :id")
	Manager findOneManagerByUserAccountId(int id);

	@Query("SELECT c FROM CodeAudit c WHERE c.id = :id")
	Project findOneProjectById(int id);

	@Query("SELECT DISTINCT p FROM UserStory us LEFT JOIN us.project p WHERE us.manager.id = :managerId")
	Collection<Project> findCreatedProjectsByManagerId(int managerId);

}
