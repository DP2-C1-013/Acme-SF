
package acme.features.manager.userstory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.project.Project;
import acme.entities.userstory.Priority;
import acme.entities.userstory.UserStory;
import acme.roles.Manager;

@Service
public class ManagerUserStoryCreateService extends AbstractService<Manager, UserStory> {

	// Internal state ---------------------------------------------------------

	private static final String			PROJECT_ID	= "projectId";
	@Autowired
	private ManagerUserStoryRepository	repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int projectId;
		Project project;

		projectId = super.getRequest().getData(ManagerUserStoryCreateService.PROJECT_ID, int.class);
		project = this.repository.findOneProjectById(projectId);
		status = project != null && project.isDraftMode() && super.getRequest().getPrincipal().hasRole(Manager.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		UserStory object;
		int projectId;
		Project project;
		Manager manager;

		projectId = super.getRequest().getData(ManagerUserStoryCreateService.PROJECT_ID, int.class);
		project = this.repository.findOneProjectById(projectId);

		manager = this.repository.findOneManagerByUserAccountId(super.getRequest().getPrincipal().getAccountId());

		object = new UserStory();
		object.setTitle("");
		object.setDescription("");
		object.setAcceptanceCriteria("");
		object.setPriority(Priority.MUST);
		object.setLink("");
		object.setDraftMode(true);
		object.setManager(manager);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final UserStory object) {
		assert object != null;

		super.bind(object, "title", "description", "estimatedCost", "acceptanceCriteria", "priority", "link");
	}

	@Override
	public void validate(final UserStory object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("estimatedCost"))
			super.state(object.getEstimatedCost().getAmount() > 0., "estimatedCost", "manager.userstory.form.error.not-positive-estimatedcost");
	}

	@Override
	public void perform(final UserStory object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final UserStory object) {
		assert object != null;

		Dataset dataset;
		SelectChoices choices;

		choices = SelectChoices.from(Priority.class, object.getPriority());

		dataset = super.unbind(object, "title", "description", "estimatedCost", "acceptanceCriteria", "priority", "link");
		dataset.put(ManagerUserStoryCreateService.PROJECT_ID, super.getRequest().getData(ManagerUserStoryCreateService.PROJECT_ID, int.class));
		dataset.put("draftMode", object.getProject().isDraftMode());
		dataset.put("priorities", choices);

		super.getResponse().addData(dataset);
	}
}
