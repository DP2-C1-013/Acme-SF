
package acme.entities.objective;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.entities.userstory.Priority;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Objective extends AbstractEntity {

	//Serialization indentifier --------------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	//Atributes ------------------------------------------------------------------------
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	@Past
	private Date				instantiationMoment;

	@NotBlank
	@Length(max = 75)
	private String				title;

	@NotBlank
	@Length(max = 100)
	private String				description;

	@NotNull
	private Priority			priority;

	@NotNull
	private Boolean				isCritical;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date				duration;

	@URL
	private String				link;

}
