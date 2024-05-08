
package acme.features.authenticated.trainingmodule;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.trainingmodule.TrainingModule;

@Repository
public interface AuthenticatedTrainingModuleRepository extends AbstractRepository {

	@Query("SELECT tm FROM TrainingModule tm WHERE tm.id = :id")
	TrainingModule findTrainingModuleById(int id);

	@Query("SELECT tm FROM TrainingModule tm WHERE tm.draftMode = false")
	Collection<TrainingModule> findAllPublishedTrainingModule();
}
