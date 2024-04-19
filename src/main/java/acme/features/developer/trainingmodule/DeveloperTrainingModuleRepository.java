
package acme.features.developer.trainingmodule;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.project.Project;
import acme.entities.trainingmodule.TrainingModule;

@Repository
public interface DeveloperTrainingModuleRepository extends AbstractRepository {

	@Query("SELECT t FROM TrainingModule t where t.developer.id = :developerId")
	Collection<TrainingModule> findDeveloperTrainingModules(int developerId);

	@Query("SELECT t FROM TrainingModule t WHERE t.id = :id")
	TrainingModule findTrainingModuleById(int id);

	@Query("SELECT DISTINCT p FROM Project p")
	Collection<Project> findAllProjects();
}
