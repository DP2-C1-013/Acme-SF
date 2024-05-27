
package acme.features.auditor.codeaudit;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.auditrecord.AuditRecord;
import acme.entities.codeaudit.CodeAudit;
import acme.entities.codeaudit.CodeAuditType;
import acme.entities.project.Project;
import acme.roles.Auditor;

@Service
public class AuditorCodeAuditUpdateService extends AbstractService<Auditor, CodeAudit> {

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
		if (project != null)
			project = this.repository.findOneProjectByCode(project.getCode());

		super.bind(object, "code", "executionDate", "type", "correctiveActions", "link");
		object.setProject(project);
		object.setDraftMode(true);
	}

	@Override
	public void validate(final CodeAudit object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			CodeAudit existing;
			existing = this.repository.findOneCodeAuditByCode(object.getCode());
			super.state(existing == null || existing.getId() == object.getId(), "code", "auditor.code-audit.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("project")) {
			Project existingProject = this.repository.findOneProjectByCode(object.getProject().getCode());
			super.state(existingProject != null && !existingProject.isDraftMode() && !object.getProject().isDraftMode(), "project", "auditor.code-audit.form.error.invalid-project");
		}

		if (!super.getBuffer().getErrors().hasErrors("executionDate")) {

			Collection<AuditRecord> records = this.repository.findManyAuditRecordsByCodeAuditId(object.getId());
			Date minDate = new Date(99, 11, 31, 23, 59);
			if (records.size() > 0) {
				Date min = this.repository.findEarliestStartDateByCodeAuditId(object.getId());
				super.state(MomentHelper.isBeforeOrEqual(object.getExecutionDate(), min) && object.getExecutionDate().after(minDate), "executionDate", "auditor.code-audit.form.error.invalid-date");
			} else
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

		SelectChoices types;
		SelectChoices projects;
		Dataset dataset;

		types = SelectChoices.from(CodeAuditType.class, object.getType());
		projects = SelectChoices.from(this.repository.findAllProjectsDraftModeFalse(), "code", object.getProject());

		dataset = super.unbind(object, "code", "executionDate", "type", "correctiveActions", "mark", "link", "draftMode");
		dataset.put("types", types);
		dataset.put("projects", projects);
		dataset.put("project", projects.getSelected());

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("PUT"))
			PrincipalHelper.handleUpdate();
	}
}
