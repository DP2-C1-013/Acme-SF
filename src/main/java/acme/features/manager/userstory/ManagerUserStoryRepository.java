
package acme.features.manager.userstory;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.project.Project;
import acme.entities.userstory.UserStory;
import acme.roles.Manager;

@Repository
public interface ManagerUserStoryRepository extends AbstractRepository {

	@Query("SELECT p FROM Project p WHERE p.id = :id")
	Project findOneProjectById(int id);

	@Query("SELECT us FROM UserStory us WHERE us.id = :id")
	UserStory findOneUserStoryById(int id);

	@Query("SELECT m FROM Manager m WHERE m.userAccount.id = :id")
	Manager findOneManagerByUserAccountId(int id);

}
