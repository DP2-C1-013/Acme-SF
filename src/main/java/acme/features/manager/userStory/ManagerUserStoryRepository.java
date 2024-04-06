
package acme.features.manager.userStory;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.project.Project;
import acme.entities.userStory.UserStory;

@Repository
public interface ManagerUserStoryRepository extends AbstractRepository {

	@Query("SELECT p FROM Project p WHERE p.id = :id")
	Project findOneProjectById(int id);

	@Query("SELECT us.project FROM UserStory us WHERE us.id = :id ")
	Project findOneProjectByUserStoryId(int id);

	@Query("SELECT us FROM UserStory us WHERE us.id = :id")
	UserStory findOneUserStoryById(int id);

	@Query("SELECT us FROM UserStory us WHERE us.project.id = :projectId")
	Collection<UserStory> findManyUserStoriesByProjectId(int projectId);

}
