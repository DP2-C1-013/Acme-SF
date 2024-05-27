
package acme.features.auditor.auditrecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.auditrecord.AuditRecord;
import acme.roles.Auditor;

@Service
public class AuditorAuditRecordListService extends AbstractService<Auditor, AuditRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorAuditRecordRepository repository;

	// AbstractService<Auditor, CodeAudit> ---------------------------


	@Override
	public void authorise() {
		boolean status;
		int codeAuditId;
		int auditorId;

		codeAuditId = this.getRequest().getData("codeAuditId", int.class);
		auditorId = this.getRequest().getPrincipal().getActiveRoleId();

		Collection<AuditRecord> auditRecords = this.repository.findManyAuditRecordsByCodeAuditId(codeAuditId);

		boolean validCodeAuditId = auditRecords.stream().allMatch(auditRecord -> auditRecord.getCodeAudit().getAuditor().getId() == auditorId);

		status = super.getRequest().getPrincipal().hasRole(Auditor.class) && validCodeAuditId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<AuditRecord> objects;
		int codeAuditId;

		codeAuditId = this.getRequest().getData("codeAuditId", int.class);

		objects = this.repository.findManyAuditRecordsByCodeAuditId(codeAuditId);

		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final AuditRecord object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "draftMode");
		dataset.put("mark", object.getMark().toString());
		dataset.put("codeAudit", object.getCodeAudit().getCode());

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<AuditRecord> objects) {
		assert objects != null;

		int codeAuditId;

		codeAuditId = this.getRequest().getData("codeAuditId", int.class);

		super.getResponse().addGlobal("codeAuditId", codeAuditId);
	}

}
