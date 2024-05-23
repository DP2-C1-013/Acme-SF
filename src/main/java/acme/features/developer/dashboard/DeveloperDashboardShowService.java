
package acme.features.developer.dashboard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.trainingmodule.TrainingModule;
import acme.entities.trainingsession.TrainingSession;
import acme.forms.DeveloperDashboard;
import acme.roles.Developer;

@Service
public class DeveloperDashboardShowService extends AbstractService<Developer, DeveloperDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	public Integer getEstimatedTotalTime(final TrainingModule tm) {
		int estimatedTotalTime = 0;
		if (!tm.isDraftMode()) {
			List<TrainingSession> ts = this.repository.findPublishedTrainingSessionsByTMId(tm.getId()).stream().toList();
			if (ts.stream().allMatch(t -> !t.isDraftMode()))
				for (TrainingSession t : ts)
					estimatedTotalTime += (t.getEndDate().getTime() - t.getStartDate().getTime()) / 3600000;
		}
		return estimatedTotalTime;
	}

	public Double findAverageTimeTrainingModulesByDeveloper(final int developerId) {
		List<TrainingModule> trainingModules = this.repository.findPublishedTrainingModulesByDeveloper(developerId).stream().toList();
		return trainingModules.stream().mapToInt(this::getEstimatedTotalTime).average().orElse(0.);
	}

	public Double findDeviationTrainingModulesByDeveloper(final int developerId) {
		List<Integer> estimatedTotalTimes = this.repository.findPublishedTrainingModulesByDeveloper(developerId).stream().mapToInt(this::getEstimatedTotalTime).boxed().toList();
		double media = estimatedTotalTimes.stream().mapToInt(Integer::intValue).average().orElse(0.0);
		double sumaDeCuadrados = estimatedTotalTimes.stream().mapToDouble(num -> Math.pow(num - media, 2)).sum();
		return Math.sqrt(sumaDeCuadrados / estimatedTotalTimes.size());
	}

	public Integer findMinimumTimeTrainingModules(final int developerId) {
		List<Integer> estimatedTotalTimes = this.repository.findPublishedTrainingModulesByDeveloper(developerId).stream().mapToInt(this::getEstimatedTotalTime).boxed().toList();
		return estimatedTotalTimes.stream().mapToInt(Integer::intValue).min().orElse(0);
	}

	public Integer findMaximumTimeTrainingModules(final int developerId) {
		List<Integer> estimatedTotalTimes = this.repository.findPublishedTrainingModulesByDeveloper(developerId).stream().mapToInt(this::getEstimatedTotalTime).boxed().toList();
		return estimatedTotalTimes.stream().mapToInt(Integer::intValue).max().orElse(0);
	}

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
		Integer minimumTimeTrainingModules;
		Integer maximumTimeTrainingModules;

		developerId = super.getRequest().getPrincipal().getActiveRoleId();

		numTrainingModulesUpdateMoment = this.repository.findNumTrainingModulesUpdateMoment(developerId);
		numTrainingSessionsLink = this.repository.findNumTrainingSessionsLink(developerId);
		averageTimeTrainingModules = this.findAverageTimeTrainingModulesByDeveloper(developerId);
		deviationTimeTrainingModules = this.findDeviationTrainingModulesByDeveloper(developerId);
		minimumTimeTrainingModules = this.findMinimumTimeTrainingModules(developerId);
		maximumTimeTrainingModules = this.findMaximumTimeTrainingModules(developerId);

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
