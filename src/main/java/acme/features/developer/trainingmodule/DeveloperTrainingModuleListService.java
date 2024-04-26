
package acme.features.developer.trainingmodule;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.trainingmodule.TrainingModule;
import acme.roles.Developer;

@Service
public class DeveloperTrainingModuleListService extends AbstractService<Developer, TrainingModule> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperTrainingModuleRepository repository;

	// AbstractService<Developer, TrainingModule> ---------------------------


	@Override
	public void authorise() {
		boolean status;
		int trainingModuleId;
		TrainingModule trainingModule;
		int developerId;

		trainingModuleId = super.getRequest().getData("trainingModuleId", int.class);
		trainingModule = this.repository.findTrainingModuleById(trainingModuleId);
		developerId = super.getRequest().getPrincipal().getActiveRoleId();

		status = super.getRequest().getPrincipal().hasRole(Developer.class) && developerId == trainingModule.getDeveloper().getId();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<TrainingModule> objects;
		int developerId;

		developerId = this.getRequest().getPrincipal().getActiveRoleId();
		objects = this.repository.findDeveloperTrainingModules(developerId);
		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final TrainingModule object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "creationMoment", "details", "difficultyLevel", "draftMode");
		dataset.put("project", object.getProject().getCode());

		super.getResponse().addData(dataset);
	}
}
