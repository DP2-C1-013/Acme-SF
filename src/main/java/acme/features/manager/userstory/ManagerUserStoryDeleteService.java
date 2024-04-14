
package acme.features.manager.userstory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.project.Project;
import acme.entities.projectuserstory.ProjectUserStory;
import acme.entities.userstory.UserStory;
import acme.features.manager.projectuserstory.ManagerProjectUserStoryRepository;
import acme.roles.Manager;

@Service
public class ManagerUserStoryDeleteService extends AbstractService<Manager, UserStory> {

	private static final String					PROJECT_ID	= "projectId";

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerUserStoryRepository			repository;

	@Autowired
	private ManagerProjectUserStoryRepository	managerProjectUserStoryRepository;

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
		status = project != null && project.isDraftMode() && super.getRequest().getPrincipal().hasRole(manager);

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
	}

	@Override
	public void perform(final UserStory object) {
		assert object != null;

		ProjectUserStory projectUserStory = this.managerProjectUserStoryRepository.findOneProjectUserStoryByUserStoryId(object.getId());

		this.managerProjectUserStoryRepository.delete(projectUserStory);
		this.repository.delete(object);
	}

	@Override
	public void unbind(final UserStory object) {
		assert object != null;

		Dataset dataset;
		Project project;
		int projectId;

		projectId = super.getRequest().getData(ManagerUserStoryDeleteService.PROJECT_ID, int.class);
		project = this.repository.findOneProjectById(projectId);

		dataset = super.unbind(object, "title", "description", "estimatedCost", "acceptanceCriteria", "priority", "link");
		dataset.put(ManagerUserStoryDeleteService.PROJECT_ID, project.getId());
		dataset.put("draftMode", project.isDraftMode());

		super.getResponse().addData(dataset);
	}
}
