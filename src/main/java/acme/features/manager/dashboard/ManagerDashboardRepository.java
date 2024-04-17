
package acme.features.manager.dashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface ManagerDashboardRepository extends AbstractRepository {

	@Query("SELECT COUNT(us) FROM UserStory us WHERE us.priority = 'MUST' AND us.manager.id = :id")
	Integer numOfMustUserStories(int id);

	@Query("SELECT COUNT(us) FROM UserStory us WHERE us.priority = 'SHOULD' AND us.manager.id = :id")
	Integer numOfShouldUserStories(int id);

	@Query("SELECT COUNT(us) FROM UserStory us WHERE us.priority = 'COULD' AND us.manager.id = :id")
	Integer numOfCouldUserStories(int id);

	@Query("SELECT COUNT(us) FROM UserStory us WHERE us.priority = 'WONT' AND us.manager.id = :id")
	Integer numOfWontUserStories(int id);

	@Query("SELECT AVG(us.estimatedCost.amount) FROM UserStory us WHERE us.manager.id = :id")
	Double averageEstimatedCost(int id);

	@Query("SELECT STDDEV(us.estimatedCost.amount) FROM UserStory us WHERE us.manager.id = :id")
	Double deviationEstimatedCost(int id);

	@Query("SELECT MIN(us.estimatedCost.amount) FROM UserStory us WHERE us.manager.id = :id")
	Double minEstimatedCost(int id);

	@Query("SELECT MAX(us.estimatedCost.amount) FROM UserStory us WHERE us.manager.id = :id")
	Double maxEstimatedCost(int id);

	@Query("SELECT AVG(p.cost.amount) FROM Project p WHERE p.manager.id = :id")
	Double averageCost(int id);

	@Query("SELECT STDDEV(p.cost.amount) FROM Project p WHERE p.manager.id = :id")
	Double deviationCost(int id);

	@Query("SELECT MIN(p.cost.amount) FROM Project p WHERE p.manager.id = :id")
	Double minCost(int id);

	@Query("SELECT MAX(p.cost.amount) FROM Project p WHERE p.manager.id = :id")
	Double maxCost(int id);

}
