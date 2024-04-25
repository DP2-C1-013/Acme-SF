
package acme.features.manager.projectuserstory;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.project.Project;
import acme.entities.projectuserstory.ProjectUserStory;
import acme.entities.userstory.UserStory;
import acme.roles.Manager;

@Repository
public interface ManagerProjectUserStoryRepository extends AbstractRepository {

	@Query("SELECT m FROM Manager m WHERE m.id = :id")
	Manager findOneManagerById(int id);

	@Query("SELECT p FROM Project p WHERE p.id = :id")
	Project findOneProjectById(int id);

	@Query("SELECT us FROM UserStory us WHERE us.id = :id")
	UserStory findOneUserStoryById(int id);

	@Query("SELECT pus FROM ProjectUserStory pus WHERE pus.id = :id")
	ProjectUserStory findOneProjectUserStoryById(int id);

	@Query("SELECT pus FROM ProjectUserStory pus WHERE pus.userStory.id = :id")
	ProjectUserStory findOneProjectUserStoryByUserStoryId(int id);

	@Query("SELECT pus FROM ProjectUserStory pus WHERE pus.project.id = :projectId AND pus.userStory.id = :userStoryId")
	ProjectUserStory findOneProjectUserStoryByProjectIdAndUserStoryId(int projectId, int userStoryId);

	@Query("SELECT p FROM Project p WHERE p.manager.id = :id AND p.draftMode = true")
	Collection<Project> findCreatedProjectsByManagerId(int id);

	@Query("SELECT us FROM UserStory us WHERE us.manager.userAccount.id = :id")
	Collection<UserStory> findManyUserStoriesByManagerId(int id);

	@Query("SELECT pus FROM ProjectUserStory pus WHERE pus.id = :id")
	Collection<ProjectUserStory> findProjectUserStoriesById(int id);

	@Query("SELECT pus FROM ProjectUserStory pus WHERE pus.project.id = :id")
	Collection<ProjectUserStory> findProjectUserStoriesByProjectId(int id);

	@Query("SELECT pus FROM UserStory us LEFT JOIN ProjectUserStory pus ON pus.userStory.id = us.id WHERE us.manager.id = :id")
	Collection<ProjectUserStory> findProjectUserStoriesByManagerId(int id);

}
