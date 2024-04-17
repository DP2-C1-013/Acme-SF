
package acme.features.manager.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
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
		Double averageEstimatedCost;
		Double deviationEstimatedCost;
		Double minEstimatedCost;
		Double maxEstimatedCost;
		Double averageCost;
		Double deviationCost;
		Double minCost;
		Double maxCost;

		managerId = super.getRequest().getPrincipal().getActiveRoleId();
		numOfMustUserStories = this.repository.numOfMustUserStories(managerId);
		numOfShouldUserStories = this.repository.numOfShouldUserStories(managerId);
		numOfCouldUserStories = this.repository.numOfCouldUserStories(managerId);
		numOfWontUserStories = this.repository.numOfWontUserStories(managerId);
		averageEstimatedCost = this.repository.averageEstimatedCost(managerId);
		deviationEstimatedCost = this.repository.deviationEstimatedCost(managerId);
		minEstimatedCost = this.repository.minEstimatedCost(managerId);
		maxEstimatedCost = this.repository.maxEstimatedCost(managerId);
		averageCost = this.repository.averageCost(managerId);
		deviationCost = this.repository.deviationCost(managerId);
		minCost = this.repository.minCost(managerId);
		maxCost = this.repository.maxCost(managerId);

		dashboard = new ManagerDashboard();
		dashboard.setNumOfMustUserStories(numOfMustUserStories);
		dashboard.setNumOfShouldUserStories(numOfShouldUserStories);
		dashboard.setNumOfCouldUserStories(numOfCouldUserStories);
		dashboard.setNumOfWontUserStories(numOfWontUserStories);
		dashboard.setAverageEstimatedCost(averageEstimatedCost);
		dashboard.setDeviationEstimatedCost(deviationEstimatedCost);
		dashboard.setMinEstimatedCost(minEstimatedCost);
		dashboard.setMaxEstimatedCost(maxEstimatedCost);
		dashboard.setAverageCost(averageCost);
		dashboard.setDeviationCost(deviationCost);
		dashboard.setMinCost(minCost);
		dashboard.setMaxCost(maxCost);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final ManagerDashboard object) {
		Dataset dataset;

		dataset = super.unbind(object, //
			"numOfMustUserStories", "numOfShouldUserStories", // 
			"numOfCouldUserStories", "numOfWontUserStories", //
			"averageEstimatedCost", "deviationEstimatedCost", //
			"minEstimatedCost", "maxEstimatedCost", //
			"averageCost", "deviationCost", //
			"minCost", "maxCost");

		super.getResponse().addData(dataset);
	}
}
