
package acme.entities.trainingsession;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.entities.trainingmodule.TrainingModule;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TrainingSession extends AbstractEntity {

	// Serialization identifier ----------------------------
	private static final long	serialVersionUID	= 1L;

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "TS-[A-Z]{1,3}-[0-9]{3}", message = "{training-session.code.error}")
	private String				code;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				startDate;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				endDate;

	@Length(max = 75)
	@NotBlank
	private String				location;

	@Length(max = 75)
	@NotBlank
	private String				instructor;

	@NotBlank
	@Email
	@Length(max = 256)
	private String				contactEmail;

	@URL
	@Length(max = 256)
	private String				optionalLink;

	@NotNull
	private boolean				draftMode;

	@ManyToOne(optional = false)
	@Valid
	@NotNull
	@OnDelete(action = OnDeleteAction.CASCADE)
	private TrainingModule		trainingModule;

}
