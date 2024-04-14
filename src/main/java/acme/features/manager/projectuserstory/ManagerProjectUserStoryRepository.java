
package acme.features.manager.projectuserstory;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.projectuserstory.ProjectUserStory;

@Repository
public interface ManagerProjectUserStoryRepository extends AbstractRepository {

	@Query("SELECT pus FROM ProjectUserStory pus WHERE pus.userStory.id = :id")
	ProjectUserStory findOneProjectUserStoryByUserStoryId(int id);

}
