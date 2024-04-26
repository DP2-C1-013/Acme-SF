
package acme.features.manager.userstory;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.project.Project;
import acme.entities.projectuserstory.ProjectUserStory;
import acme.entities.userstory.Priority;
import acme.entities.userstory.UserStory;
import acme.features.manager.projectuserstory.ManagerProjectUserStoryRepository;
import acme.roles.Manager;

@Service
public class ManagerUserStoryCreateInProjectService extends AbstractService<Manager, UserStory> {

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
		int projectId;
		Project project;
		Manager manager;

		projectId = super.getRequest().getData(ManagerUserStoryCreateInProjectService.PROJECT_ID, int.class);
		project = this.repository.findOneProjectById(projectId);
		manager = this.repository.findOneManagerById(super.getRequest().getPrincipal().getActiveRoleId());
		status = project != null && project.isDraftMode() && super.getRequest().getPrincipal().hasRole(manager);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		UserStory object;
		Manager manager;

		manager = this.repository.findOneManagerById(super.getRequest().getPrincipal().getActiveRoleId());

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

		if (!super.getBuffer().getErrors().hasErrors("estimatedCost")) {
			super.state(object.getEstimatedCost().getAmount() > 0., "estimatedCost", "manager.userstory.form.error.not-positive-estimatedcost");
			List<String> currencies = Arrays.asList(this.repository.findSystemCurrencies().get(0).getAcceptedCurrencies().split(","));
			super.state(currencies.stream().anyMatch(c -> c.equals(object.getEstimatedCost().getCurrency())), "estimatedCost", "manager.userstory.form.error.invalid-currency");
		}
	}

	@Override
	public void perform(final UserStory object) {
		assert object != null;

		ProjectUserStory pus = new ProjectUserStory();
		int projectId;
		Project project;

		projectId = super.getRequest().getData(ManagerUserStoryCreateInProjectService.PROJECT_ID, int.class);
		project = this.repository.findOneProjectById(projectId);

		pus.setProject(project);
		pus.setUserStory(object);

		this.repository.save(object);
		this.managerProjectUserStoryRepository.save(pus);
	}

	@Override
	public void unbind(final UserStory object) {
		assert object != null;

		Dataset dataset;
		SelectChoices choices;
		Project project;
		int projectId;

		choices = SelectChoices.from(Priority.class, object.getPriority());

		projectId = super.getRequest().getData(ManagerUserStoryCreateInProjectService.PROJECT_ID, int.class);
		project = this.repository.findOneProjectById(projectId);

		dataset = super.unbind(object, "title", "description", "estimatedCost", "acceptanceCriteria", "priority", "link");
		dataset.put(ManagerUserStoryCreateInProjectService.PROJECT_ID, super.getRequest().getData(ManagerUserStoryCreateInProjectService.PROJECT_ID, int.class));
		dataset.put("draftMode", project.isDraftMode());
		dataset.put("priorities", choices);

		super.getResponse().addData(dataset);
	}
}
