
package acme.features.client.porgresslog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractService;
import acme.entities.progresslog.ProgressLog;
import acme.roles.client.Client;

@Service
public class ClientProgressLogPublishService extends AbstractService<Client, ProgressLog> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private ClientProgressLogRepository repository;
	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		var request = super.getRequest();

		int clientId;
		int progressLogId;

		progressLogId = request.getData("id", int.class);

		clientId = request.getPrincipal().getActiveRoleId();

		ProgressLog object = this.repository.findOneProgressLogById(progressLogId);

		status = object != null && request.getPrincipal().hasRole(Client.class) && object.getContract().getClient().getId() == clientId && object.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ProgressLog object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneProgressLogById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final ProgressLog object) {

		assert object != null;

		super.bind(object, "recordId", "completeness", "comment", "registrationMoment", "responsiblePerson");
	}

	@Override
	public void validate(final ProgressLog object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("recordId")) {
			final int id = super.getRequest().getData("id", int.class);
			final boolean duplicated = this.repository.findAllProgressLogs().stream().filter(e -> e.getId() != id).anyMatch(e -> e.getRecordId().equals(object.getRecordId()));

			super.state(!duplicated, "recordId", "client.progress.form.error.duplicated-code");
		}
	}

	@Override
	public void perform(final ProgressLog object) {
		assert object != null;

		object.setDraftMode(false);

		this.repository.save(object);
	}

	@Override
	public void unbind(final ProgressLog object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "recordId", "completeness", "comment", "registrationMoment", "responsiblePerson");
		dataset.put("sponsorship", object.getContract().getCode());
		dataset.put("sponsorshipDraftMode", object.getContract().getDraftMode());
		dataset.put("sponsorshipId", object.getContract().getId());

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("PUT"))
			PrincipalHelper.handleUpdate();
	}

}
