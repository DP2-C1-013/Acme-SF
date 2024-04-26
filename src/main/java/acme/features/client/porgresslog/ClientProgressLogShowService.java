
package acme.features.client.porgresslog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.progresslog.ProgressLog;
import acme.roles.client.Client;

@Service
public class ClientProgressLogShowService extends AbstractService<Client, ProgressLog> {

	@Autowired
	private ClientProgressLogRepository repository;


	@Override
	public void authorise() {
		boolean status;

		var request = super.getRequest();

		int clientId;
		int progressLogId;

		progressLogId = request.getData("id", int.class);

		clientId = request.getPrincipal().getActiveRoleId();

		ProgressLog object = this.repository.findOneProgressLogById(progressLogId);

		status = object != null && request.getPrincipal().hasRole(Client.class) && object.getContract().getClient().getId() == clientId;

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
	public void unbind(final ProgressLog object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "recordId", "completeness", "comment", "registrationMoment", "responsiblePerson");
		dataset.put("progressLogDraftMode", object.getContract().getDraftMode());
		dataset.put("progressLogId", object.getContract().getId());

		super.getResponse().addData(dataset);
	}
}
