
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
public class ManagerUserStoryUpdateService extends AbstractService<Manager, UserStory> {

	private static final String			PROJECT_ID	= "projectId";

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerUserStoryRepository	repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int userStoryId;
		Project project;
		Manager manager;
		UserStory userStory;

		manager = this.repository.findOneManagerById(super.getRequest().getPrincipal().getActiveRoleId());
		userStoryId = super.getRequest().getData("id", int.class);
		userStory = this.repository.findOneUserStoryById(userStoryId);
		project = this.repository.findOneProjectByUserStoryId(userStoryId);
		status = project != null && userStory.isDraftMode() && super.getRequest().getPrincipal().hasRole(manager);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		UserStory object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneUserStoryById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final UserStory object) {
		assert object != null;

		super.bind(object, "title", "description", "estimatedCost", "acceptanceCriteria", "priority", "link", "draftMode");
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
		Project project;

		choices = SelectChoices.from(Priority.class, object.getPriority());

		project = this.repository.findOneProjectByUserStoryId(object.getId());

		dataset = super.unbind(object, "title", "description", "estimatedCost", "acceptanceCriteria", "priority", "link");
		dataset.put(ManagerUserStoryUpdateService.PROJECT_ID, project.getId());
		dataset.put("draftMode", object.isDraftMode());
		dataset.put("priorities", choices);

		super.getResponse().addData(dataset);
	}
}
