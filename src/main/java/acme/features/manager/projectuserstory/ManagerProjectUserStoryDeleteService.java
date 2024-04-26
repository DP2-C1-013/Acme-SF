
package acme.features.manager.projectuserstory;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.project.Project;
import acme.entities.projectuserstory.ProjectUserStory;
import acme.entities.userstory.UserStory;
import acme.roles.Manager;

@Service
public class ManagerProjectUserStoryDeleteService extends AbstractService<Manager, ProjectUserStory> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerProjectUserStoryRepository repository;

	// AbstractService<Manager, ProjectUserStory> ---------------------------


	@Override
	public void authorise() {
		boolean status;
		int projectUserStoryId;
		ProjectUserStory projectUserStory;
		Manager manager;

		projectUserStoryId = super.getRequest().getData("id", int.class);
		projectUserStory = this.repository.findOneProjectUserStoryById(projectUserStoryId);
		manager = projectUserStory == null ? null : projectUserStory.getProject().getManager();
		status = projectUserStory != null && super.getRequest().getPrincipal().hasRole(manager);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ProjectUserStory object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneProjectUserStoryById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final ProjectUserStory object) {
		assert object != null;

		super.bind(object, "userStory", "project");
	}

	@Override
	public void validate(final ProjectUserStory object) {
		assert object != null;

		Project project = object.getProject();

		if (!super.getBuffer().getErrors().hasErrors("project"))
			super.state(project.isDraftMode(), "project", "manager.projectuserstory.form.error.delete-published-project");
	}

	@Override
	public void perform(final ProjectUserStory object) {
		assert object != null;

		this.repository.delete(object);
	}

	@Override
	public void unbind(final ProjectUserStory object) {
		assert object != null;

		Collection<UserStory> userStories;
		Collection<Project> projects;
		SelectChoices choicesUserStories;
		SelectChoices choicesProjects;
		Dataset dataset;
		int managerId;

		managerId = super.getRequest().getPrincipal().getActiveRoleId();

		userStories = this.repository.findManyUserStoriesByManagerId(managerId);
		choicesUserStories = SelectChoices.from(userStories, "title", object.getUserStory());

		projects = this.repository.findManyProjectsByManagerId(managerId);
		choicesProjects = SelectChoices.from(projects, "code", object.getProject());

		dataset = super.unbind(object, "userStory", "project");
		dataset.put("userStory", choicesUserStories.getSelected().getKey());
		dataset.put("userStories", choicesUserStories);
		dataset.put("project", choicesProjects.getSelected().getKey());
		dataset.put("projects", choicesProjects);

		super.getResponse().addData(dataset);
	}
}
