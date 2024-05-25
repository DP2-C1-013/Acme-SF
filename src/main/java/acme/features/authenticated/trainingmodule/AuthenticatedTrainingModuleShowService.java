
package acme.features.authenticated.trainingmodule;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.trainingmodule.TrainingModule;
import acme.entities.trainingsession.TrainingSession;

@Service
public class AuthenticatedTrainingModuleShowService extends AbstractService<Authenticated, TrainingModule> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedTrainingModuleRepository repository;

	// AbstractService<Authenticated, TrainingModule> ---------------------------


	public Integer getEstimatedTotalTime(final TrainingModule tm) {
		int estimatedTotalTime = 0;
		List<TrainingSession> ts = this.repository.findTrainingSessionsByTMId(tm.getId()).stream().toList();

		for (TrainingSession t : ts)
			estimatedTotalTime += (t.getEndDate().getTime() - t.getStartDate().getTime()) / 3600000;
		return estimatedTotalTime;
	}

	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Authenticated.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrainingModule object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findTrainingModuleById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final TrainingModule object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "creationMoment", "details", "difficultyLevel", "updateMoment", "optionalLink", "draftMode", "project", "developer");
		dataset.put("developer", object.getDeveloper().getUserAccount().getUsername());
		dataset.put("estimatedTotalTime", this.getEstimatedTotalTime(object));
		dataset.put("project", object.getProject().getCode());

		super.getResponse().addData(dataset);
	}
}
