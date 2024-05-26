
package acme.entities.codeaudit;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.entities.auditrecord.AuditMark;
import acme.entities.project.Project;
import acme.roles.Auditor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "code"), @Index(columnList = "auditor_id"), @Index(columnList = "id"), @Index(columnList = "draftMode"), @Index(columnList = "id, draftMode")
})
public class CodeAudit extends AbstractEntity {

	//Serialization indentifier --------------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	//Atributes ------------------------------------------------------------------------

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "[A-Z]{1,3}-[0-9]{3}", message = "{validation.codeAudit.code}")
	private String				code;

	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	private Date				executionDate;

	@NotNull
	private CodeAuditType		type;

	@NotBlank
	@Length(max = 100)
	private String				correctiveActions;

	//Nullable in case that the code audit does not have any audit records
	private AuditMark			mark;

	@URL
	@Length(max = 255)
	private String				link;

	@NotNull
	private Boolean				draftMode;

	@NotNull
	@Valid
	@ManyToOne()
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Project				project;

	@NotNull
	@Valid
	@ManyToOne()
	private Auditor				auditor;

}
