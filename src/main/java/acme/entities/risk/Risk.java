
package acme.entities.risk;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Risk extends AbstractEntity {

	// Serialization identifier ----------------------------
	private static final long	serialVersionUID	= 1L;

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "R-[0-9]{3}", message = "{risk.reference.error}")
	private String				reference;

	@PastOrPresent
	@Temporal(TemporalType.DATE)
	private Date				idDate;

	@NotNull
	@PositiveOrZero
	@Digits(integer = Integer.MAX_VALUE, fraction = Integer.MAX_VALUE)
	private Double				impact;

	@NotNull
	@DecimalMin("0.0")
	@DecimalMax("1.0")
	@Digits(integer = 1, fraction = 6)
	private Double				probability;

	@NotBlank
	@Length(max = 100)
	private String				description;

	@URL
	@Length(max = 256)
	private String				optionalLink;


	public Double value() {
		return this.impact * this.probability;
	}
}
