
package acme.features.manager.userstory;

import java.util.Collection;

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

	@Query("SELECT m FROM Manager m WHERE m.id = :id")
	Manager findOneManagerById(int id);

	@Query("SELECT pus.project FROM ProjectUserStory pus WHERE pus.userStory.id = :id")
	Project findOneProjectByUserStoryId(int id);

	@Query("SELECT us FROM ProjectUserStory pus LEFT JOIN UserStory us ON pus.userStory.id = us.id WHERE pus.project.id = :id")
	Collection<UserStory> findManyUserStoriesByProjectId(int id);

	@Query("SELECT us FROM UserStory us WHERE us.manager.userAccount.id = :id")
	Collection<UserStory> findManyUserStoriesByManagerId(int id);

}
