
package acme.features.auditor.codeaudit;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.auditrecord.AuditRecord;
import acme.entities.codeaudit.CodeAudit;
import acme.entities.codeaudit.CodeAuditType;
import acme.entities.project.Project;
import acme.roles.Auditor;

@Service
public class AuditorCodeAuditDeleteService extends AbstractService<Auditor, CodeAudit> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorCodeAuditRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		var request = super.getRequest();

		int auditorId;
		int codeAuditId;

		codeAuditId = request.getData("id", int.class);

		auditorId = request.getPrincipal().getActiveRoleId();

		CodeAudit object = this.repository.findOneCodeAuditById(codeAuditId);

		status = object != null && request.getPrincipal().hasRole(Auditor.class) && object.getAuditor().getId() == auditorId && object.getDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		CodeAudit object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneCodeAuditById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final CodeAudit object) {
		assert object != null;

		Project project;
		project = this.getRequest().getData("project", Project.class);

		super.bind(object, "code", "executionDate", "type", "correctiveActions", "mark", "link");
		object.setProject(project);

	}

	@Override
	public void validate(final CodeAudit object) {
		assert object != null;

		int id = super.getRequest().getData("id", int.class);
		int numPublishedAuditRecord = this.repository.findNumberPublishedAuditRecordsByCodeAuditId(id);
		super.state(numPublishedAuditRecord == 0, "*", "auditor.code-audit.form.error.published-audit-records-cannot-delete");
		boolean status;
		status = object.getDraftMode();

		super.state(status, "*", "auditor.codeAudit.delete.is-draftMode");
	}

	@Override
	public void perform(final CodeAudit object) {
		assert object != null;

		Collection<AuditRecord> auditRecords = this.repository.findManyAuditRecordsByCodeAuditId(object.getId());

		this.repository.deleteAll(auditRecords);
		this.repository.delete(object);
	}

	@Override
	public void unbind(final CodeAudit object) {
		assert object != null;

		SelectChoices types;
		SelectChoices projects;
		Dataset dataset;

		types = SelectChoices.from(CodeAuditType.class, object.getType());
		projects = SelectChoices.from(this.repository.findAllProjectsDraftModeFalse(), "code", object.getProject());

		dataset = super.unbind(object, "code", "executionDate", "type", "correctiveActions", "mark", "link", "draftMode");
		dataset.put("types", types);
		dataset.put("projects", projects);
		dataset.put("project", projects.getSelected().getKey());

		super.getResponse().addData(dataset);
	}
}
