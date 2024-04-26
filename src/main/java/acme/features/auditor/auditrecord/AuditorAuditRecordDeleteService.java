
package acme.features.auditor.auditrecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.auditrecord.AuditRecord;
import acme.roles.Auditor;

@Service
public class AuditorAuditRecordDeleteService extends AbstractService<Auditor, AuditRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorAuditRecordRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		var request = super.getRequest();

		int auditorId;
		int auditRecordId;

		auditRecordId = request.getData("id", int.class);

		auditorId = request.getPrincipal().getActiveRoleId();

		AuditRecord object = this.repository.findOneAuditRecordById(auditRecordId);

		status = object != null && request.getPrincipal().hasRole(Auditor.class) && object.getCodeAudit().getAuditor().getId() == auditorId && object.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AuditRecord object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneAuditRecordById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final AuditRecord object) {
		assert object != null;

		super.bind(object, "code", "startDate", "endDate", "mark", "link", "draftMode");

	}

	@Override
	public void validate(final AuditRecord object) {
		assert object != null;
	}

	@Override
	public void perform(final AuditRecord object) {
		assert object != null;

		this.repository.delete(object);
	}

	@Override
	public void unbind(final AuditRecord object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "startDate", "endDate", "mark", "link", "draftMode");
		dataset.put("auditorship", object.getCodeAudit().getCode());
		dataset.put("auditorshipDraftMode", object.getCodeAudit().getDraftMode());
		dataset.put("auditorshipId", object.getCodeAudit().getId());

		super.getResponse().addData(dataset);
	}
}
