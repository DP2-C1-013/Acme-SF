
package acme.features.auditor.codeaudit;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.auditrecord.AuditMark;
import acme.entities.auditrecord.AuditRecord;
import acme.entities.codeaudit.CodeAudit;
import acme.entities.codeaudit.CodeAuditType;
import acme.entities.project.Project;
import acme.roles.Auditor;

@Service
public class AuditorCodeAuditCreateService extends AbstractService<Auditor, CodeAudit> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorCodeAuditRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Auditor.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		CodeAudit object;
		Auditor auditor;

		auditor = this.repository.findOneAuditorByAuditorId(this.getRequest().getPrincipal().getActiveRoleId());

		object = new CodeAudit();
		object.setDraftMode(true);
		object.setAuditor(auditor);
		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final CodeAudit object) {
		assert object != null;

		Project project;

		project = this.getRequest().getData("project", Project.class);
		if (project != null)
			project = this.repository.findOneProjectByCode(project.getCode());

		super.bind(object, "code", "executionDate", "type", "correctiveActions", "mark", "link");
		object.setProject(project);
		object.setDraftMode(true);
	}

	@Override
	public void validate(final CodeAudit object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			CodeAudit existing;
			existing = this.repository.findOneCodeAuditByCode(object.getCode());
			super.state(existing == null, "code", "auditor.code-audit.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("project")) {
			Project existingProject = this.repository.findOneProjectByCode(object.getProject().getCode());
			super.state(existingProject != null && !existingProject.isDraftMode() && !object.getProject().isDraftMode(), "project", "auditor.code-audit.form.error.invalid-project");
		}

		if (!super.getBuffer().getErrors().hasErrors("executionDate")) {
			Date minDate = new Date(99, 12, 31, 23, 59);
			super.state(object.getExecutionDate().after(minDate), "executionDate", "auditor.code-audit.form.error.invalid-date");
		}

	}

	@Override
	public void perform(final CodeAudit object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final CodeAudit object) {
		assert object != null;
		AuditRecord auditRecord = new AuditRecord();

		SelectChoices types;
		SelectChoices marks;
		SelectChoices projects;
		Dataset dataset;

		types = SelectChoices.from(CodeAuditType.class, object.getType());
		marks = SelectChoices.from(AuditMark.class, auditRecord.getMark());
		projects = SelectChoices.from(this.repository.findAllProjectsDraftModeFalse(), "code", object.getProject());

		dataset = super.unbind(object, "code", "executionDate", "type", "correctiveActions", "mark", "link", "draftMode");
		dataset.put("types", types);
		dataset.put("marks", marks);
		dataset.put("projects", projects);
		dataset.put("project", projects.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
