
package acme.features.manager.project;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.project.Project;
import acme.entities.userstory.UserStory;
import acme.roles.Manager;

@Service
public class ManagerProjectShowService extends AbstractService<Manager, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerProjectRepository repository;

	// AbstractService<Manager, Project> ---------------------------


	@Override
	public void authorise() {
		boolean status;
		int projectId;
		Project project;
		Manager manager;

		projectId = super.getRequest().getData("id", int.class);
		project = this.repository.findOneProjectById(projectId);
		manager = project == null ? null : project.getManager();

		status = project != null && super.getRequest().getPrincipal().hasRole(manager);

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Project object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneProjectById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Project object) {
		assert object != null;

		Dataset dataset;
		final boolean showPublish;
		Collection<UserStory> userStories;

		dataset = super.unbind(object, "code", "title", "abstractText", "hasFatalErrors", "cost", "link", "draftMode");

		userStories = this.repository.findManyUserStoriesByProjectId(object.getId());
		boolean allUserStoriesPublished = userStories.stream().allMatch(us -> !us.isDraftMode());

		showPublish = !userStories.isEmpty() && allUserStoriesPublished && !object.isHasFatalErrors();

		super.getResponse().addGlobal("showPublish", showPublish);

		super.getResponse().addData(dataset);
	}

}
