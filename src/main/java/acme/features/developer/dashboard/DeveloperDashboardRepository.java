
package acme.features.developer.dashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface DeveloperDashboardRepository extends AbstractRepository {

	@Query("select count(tm) from TrainingModule tm where tm.updateMoment != null and tm.developer.id = :id")
	Integer findNumTrainingModulesUpdateMoment(int id);

	@Query("select count(ts) from TrainingSession ts where ts.optionalLink != null and ts.trainingModule.developer.id = :id")
	Integer findNumTrainingSessionsLink(int id);

	@Query("select avg(tm.estimatedTotalTime) from TrainingModule tm where tm.developer.id = :id")
	Double findAverageTimeTrainingModules(int id);

	@Query("select stddev(tm.estimatedTotalTime) from TrainingModule tm where tm.developer.id = :id")
	Double findDeviationTimeTrainingModules(int id);

	@Query("select min(tm.estimatedTotalTime) from TrainingModule tm where tm.developer.id = :id")
	Double findMinimumTimeTrainingModules(int id);

	@Query("select max(tm.estimatedTotalTime) from TrainingModule tm where tm.developer.id = :id")
	Double findMaximumTimeTrainingModules(int id);
}
