
package acme.features.developer.trainingmodule;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.project.Project;
import acme.entities.trainingmodule.TrainingModule;
import acme.entities.trainingsession.TrainingSession;
import acme.roles.Developer;

@Repository
public interface DeveloperTrainingModuleRepository extends AbstractRepository {

	@Query("SELECT t FROM TrainingModule t where t.developer.id = :developerId")
	Collection<TrainingModule> findDeveloperTrainingModules(int developerId);

	@Query("SELECT t FROM TrainingModule t WHERE t.id = :id")
	TrainingModule findTrainingModuleById(int id);

	@Query("SELECT DISTINCT p FROM Project p")
	Collection<Project> findAllProjects();

	@Query("select p from Project p where p.id = :projectId")
	Project findProjectById(int projectId);

	@Query("SELECT d FROM Developer d where d.id = :developerId")
	Developer findDeveloperById(int developerId);

	@Query("SELECT t FROM TrainingModule t WHERE t.code = :code")
	TrainingModule findTrainingModuleByCode(String code);

	@Query("SELECT p FROM Project p WHERE p.code = :code")
	Project findOneProjectByCode(String code);

	@Query("SELECT DISTINCT p FROM Project p WHERE p.draftMode = false")
	Collection<Project> findAllProjectsDraftModeFalse();

	@Query("SELECT ts FROM TrainingSession ts WHERE ts.trainingModule.id = :id")
	Collection<TrainingSession> findTrainingSessionsByTMId(int id);

	@Query("select count(ts) from TrainingSession ts where ts.trainingModule.id = :id and ts.draftMode = false")
	Integer findNumTSPublishedForTMById(int id);

	@Query("select tm from TrainingModule tm where tm.draftMode = false and tm.developer.id = :developerId")
	Collection<TrainingModule> findPublishedTrainingModulesByDeveloper(int developerId);

}
