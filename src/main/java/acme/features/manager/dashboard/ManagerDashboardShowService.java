
package acme.features.manager.dashboard;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.datatypes.Statistics;
import acme.forms.ManagerDashboard;
import acme.roles.Manager;

@Service
public class ManagerDashboardShowService extends AbstractService<Manager, ManagerDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Manager.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ManagerDashboard dashboard;
		int managerId;
		Integer numOfMustUserStories;
		Integer numOfShouldUserStories;
		Integer numOfCouldUserStories;
		Integer numOfWontUserStories;

		managerId = super.getRequest().getPrincipal().getActiveRoleId();
		numOfMustUserStories = this.repository.numOfMustUserStories(managerId);
		numOfShouldUserStories = this.repository.numOfShouldUserStories(managerId);
		numOfCouldUserStories = this.repository.numOfCouldUserStories(managerId);
		numOfWontUserStories = this.repository.numOfWontUserStories(managerId);

		final Map<String, Statistics> userStoryEstimatedCostStatistics = new HashMap<>();

		final Map<String, Statistics> projectCostStatistics = new HashMap<>();

		for (String currency : this.repository.findSystemCurrencies().get(0).getAcceptedCurrencies().split(",")) {
			Double averageEstimatedCost = this.repository.averageEstimatedCost(managerId, currency);
			Double deviationEstimatedCost = this.repository.deviationEstimatedCost(managerId, currency);
			Double minEstimatedCost = this.repository.minEstimatedCost(managerId, currency);
			Double maxEstimatedCost = this.repository.maxEstimatedCost(managerId, currency);

			Double averageCost = this.repository.averageCost(managerId, currency);
			Double deviationCost = this.repository.deviationCost(managerId, currency);
			Double minCost = this.repository.minCost(managerId, currency);
			Double maxCost = this.repository.maxCost(managerId, currency);

			final Statistics userStoryStatistics = new Statistics();
			userStoryStatistics.setAverage(averageEstimatedCost);
			userStoryStatistics.setDeviation(deviationEstimatedCost);
			userStoryStatistics.setMaximum(maxEstimatedCost);
			userStoryStatistics.setMinimum(minEstimatedCost);

			final Statistics projectStatistics = new Statistics();
			projectStatistics.setAverage(averageCost);
			projectStatistics.setDeviation(deviationCost);
			projectStatistics.setMaximum(maxCost);
			projectStatistics.setMinimum(minCost);

			userStoryEstimatedCostStatistics.put(currency, userStoryStatistics);
			projectCostStatistics.put(currency, projectStatistics);

		}

		dashboard = new ManagerDashboard();
		dashboard.setNumOfMustUserStories(numOfMustUserStories);
		dashboard.setNumOfShouldUserStories(numOfShouldUserStories);
		dashboard.setNumOfCouldUserStories(numOfCouldUserStories);
		dashboard.setNumOfWontUserStories(numOfWontUserStories);
		dashboard.setUserStoryEstimatedCostStatistics(userStoryEstimatedCostStatistics);
		dashboard.setProjectCostStatistics(projectCostStatistics);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final ManagerDashboard object) {
		Dataset dataset;

		dataset = super.unbind(object, //
			"numOfMustUserStories", "numOfShouldUserStories", // 
			"numOfCouldUserStories", "numOfWontUserStories", //
			"userStoryEstimatedCostStatistics", "projectCostStatistics");

		super.getResponse().addData(dataset);
	}
}
