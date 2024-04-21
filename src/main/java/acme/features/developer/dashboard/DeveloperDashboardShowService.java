
package acme.features.developer.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.forms.DeveloperDashboard;
import acme.roles.Developer;

@Service
public class DeveloperDashboardShowService extends AbstractService<Developer, DeveloperDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Developer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		DeveloperDashboard dashboard;
		int developerId;
		Integer numTrainingModulesUpdateMoment;
		Integer numTrainingSessionsLink;
		Double averageTimeTrainingModules;
		Double deviationTimeTrainingModules;
		Double minimumTimeTrainingModules;
		Double maximumTimeTrainingModules;

		developerId = super.getRequest().getPrincipal().getActiveRoleId();
		numTrainingModulesUpdateMoment = this.repository.findNumTrainingModulesUpdateMoment(developerId);
		numTrainingSessionsLink = this.repository.findNumTrainingSessionsLink(developerId);
		averageTimeTrainingModules = this.repository.findAverageTimeTrainingModules(developerId);
		deviationTimeTrainingModules = this.repository.findDeviationTimeTrainingModules(developerId);
		minimumTimeTrainingModules = this.repository.findMinimumTimeTrainingModules(developerId);
		maximumTimeTrainingModules = this.repository.findMaximumTimeTrainingModules(developerId);

		dashboard = new DeveloperDashboard();
		dashboard.setNumTrainingModulesUpdateMoment(numTrainingModulesUpdateMoment);
		dashboard.setNumTrainingSessionsLink(numTrainingSessionsLink);
		dashboard.setAverageTimeTrainingModules(averageTimeTrainingModules);
		dashboard.setDeviationTimeTrainingModules(deviationTimeTrainingModules);
		dashboard.setMinimumTimeTrainingModules(minimumTimeTrainingModules);
		dashboard.setMaximumTimeTrainingModules(maximumTimeTrainingModules);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final DeveloperDashboard object) {
		Dataset dataset;

		dataset = super.unbind(object, //
			"numTrainingModulesUpdateMoment", "numTrainingSessionsLink", // 
			"averageTimeTrainingModules", "deviationTimeTrainingModules", //
			"minimumTimeTrainingModules", "maximumTimeTrainingModules");

		super.getResponse().addData(dataset);
	}
}
