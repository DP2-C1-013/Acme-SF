
package acme.features.auditor.auditrecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.auditrecord.AuditMark;
import acme.entities.auditrecord.AuditRecord;
import acme.roles.Auditor;

@Service
public class AuditorAuditRecordShowService extends AbstractService<Auditor, AuditRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorAuditRecordRepository repository;

	// AbstractService<Authenticated, Claim> ---------------------------


	@Override
	public void authorise() {
		boolean status;

		var request = super.getRequest();

		int auditorId;
		int auditRecordId;

		auditRecordId = request.getData("id", int.class);

		auditorId = request.getPrincipal().getActiveRoleId();

		AuditRecord object = this.repository.findOneAuditRecordById(auditRecordId);

		status = object != null && request.getPrincipal().hasRole(Auditor.class) && object.getCodeAudit().getAuditor().getId() == auditorId;

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
	public void unbind(final AuditRecord object) {
		assert object != null;

		SelectChoices marks;
		Dataset dataset;

		marks = SelectChoices.from(AuditMark.class, object.getMark());

		dataset = super.unbind(object, "code", "startDate", "endDate", "mark", "link", "draftMode");
		dataset.put("marks", marks);
		dataset.put("codeAudit", object.getCodeAudit().getCode());
		dataset.put("codeAuditDraftMode", object.getCodeAudit().getDraftMode());

		super.getResponse().addData(dataset);
	}
}
