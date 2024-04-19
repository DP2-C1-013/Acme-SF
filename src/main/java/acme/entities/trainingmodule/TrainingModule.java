
package acme.entities.trainingmodule;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.entities.project.Project;
import acme.roles.Developer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class TrainingModule extends AbstractEntity {

	// Serialization identifier ----------------------------
	private static final long	serialVersionUID	= 1L;

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "[A-Z]{1,3}-[0-9]{3}", message = "{trainingmodule.code.error}")
	private String				code;

	@NotNull
	@PastOrPresent
	@Temporal(TemporalType.TIMESTAMP)
	private Date				creationMoment;

	@NotBlank
	@Length(max = 100)
	private String				details;

	@NotNull
	private DifficultyLevel		difficultyLevel;

	@PastOrPresent
	@Temporal(TemporalType.TIMESTAMP)
	private Date				updateMoment;

	@URL
	@Length(max = 256)
	private String				optionalLink;

	@NotNull
	@Min(value = 1)
	private Integer				estimatedTotalTime;

	@NotNull
	private boolean				draftMode;

	// Relationships --------------------------------------------------------------------

	@NotNull
	@Valid
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Project				project;

	@NotNull
	@Valid
	@ManyToOne
	private Developer			developer;
}
