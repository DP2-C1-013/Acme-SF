
package acme.entities.banner;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Banner extends AbstractEntity {

	//Serialization indentifier --------------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	//Atributes ------------------------------------------------------------------------

	@NotNull
	@PastOrPresent
	@Temporal(TemporalType.TIMESTAMP)
	private Date				instantiationMoment;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				startDisplay;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				endDisplay;

	@NotBlank
	@URL
	@Length(max = 255)
	private String				picture;

	@NotBlank
	@Length(max = 75)
	private String				slogan;

	@NotNull
	@URL
	@Length(max = 255)
	private String				link;

}
