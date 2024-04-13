
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
public class ManagerUserStoryShowService extends AbstractService<Manager, UserStory> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerUserStoryRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int userStoryId;
		Project project;
		Manager manager;

		manager = this.repository.findOneManagerById(super.getRequest().getPrincipal().getActiveRoleId());
		userStoryId = super.getRequest().getData("id", int.class);
		project = this.repository.findOneProjectByUserStoryId(userStoryId);
		status = project != null && (!project.isDraftMode() || super.getRequest().getPrincipal().hasRole(manager));

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
	public void unbind(final UserStory object) {
		assert object != null;

		Dataset dataset;
		SelectChoices choices;
		int projectId;

		choices = SelectChoices.from(Priority.class, object.getPriority());
		projectId = this.repository.findOneProjectByUserStoryId(object.getId()).getId();

		dataset = super.unbind(object, "title", "description", "estimatedCost", "acceptanceCriteria", "priority", "link", "draftMode");
		dataset.put("projectId", projectId);
		dataset.put("priorities", choices);

		super.getResponse().addData(dataset);
	}
}
