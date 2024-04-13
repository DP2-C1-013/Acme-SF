
package acme.features.manager.project;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.project.Project;
import acme.roles.Manager;

@Repository
public interface ManagerProjectRepository extends AbstractRepository {

	@Query("SELECT m FROM Manager m WHERE m.userAccount.id = :id")
	Manager findOneManagerByUserAccountId(int id);

	@Query("SELECT p FROM Project p WHERE p.id = :id")
	Project findOneProjectById(int id);

}
