
package acme.features.manager.dashboard;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.systemconfiguration.SystemConfiguration;

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

	@Query("SELECT AVG(us.estimatedCost.amount) FROM UserStory us WHERE us.manager.id = :id AND us.estimatedCost.currency = :currency")
	Double averageEstimatedCost(int id, String currency);

	@Query("SELECT STDDEV(us.estimatedCost.amount) FROM UserStory us WHERE us.manager.id = :id AND us.estimatedCost.currency = :currency")
	Double deviationEstimatedCost(int id, String currency);

	@Query("SELECT MIN(us.estimatedCost.amount) FROM UserStory us WHERE us.manager.id = :id AND us.estimatedCost.currency = :currency")
	Double minEstimatedCost(int id, String currency);

	@Query("SELECT MAX(us.estimatedCost.amount) FROM UserStory us WHERE us.manager.id = :id AND us.estimatedCost.currency = :currency")
	Double maxEstimatedCost(int id, String currency);

	@Query("SELECT AVG(p.cost.amount) FROM Project p WHERE p.manager.id = :id AND p.cost.currency = :currency")
	Double averageCost(int id, String currency);

	@Query("SELECT STDDEV(p.cost.amount) FROM Project p WHERE p.manager.id = :id AND p.cost.currency = :currency")
	Double deviationCost(int id, String currency);

	@Query("SELECT MIN(p.cost.amount) FROM Project p WHERE p.manager.id = :id AND p.cost.currency = :currency")
	Double minCost(int id, String currency);

	@Query("SELECT MAX(p.cost.amount) FROM Project p WHERE p.manager.id = :id AND p.cost.currency = :currency")
	Double maxCost(int id, String currency);

	@Query("SELECT sc FROM SystemConfiguration sc")
	List<SystemConfiguration> findSystemCurrencies();

}
