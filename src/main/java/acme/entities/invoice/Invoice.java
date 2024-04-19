
package acme.entities.invoice;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.client.data.datatypes.Money;
import acme.entities.sponsorship.Sponsorship;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Invoice extends AbstractEntity {

	// Serialization indentifier --------------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Atributes ------------------------------------------------------------------------

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "IN-[0-9]{4}-[0-9]{4}", message = "{validation.claim.code}")
	private String				code;

	@NotNull
	@PastOrPresent
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationTime;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				dueDate;

	@NotNull
	@Valid
	private Money				quantity;

	@NotNull
	@Min(0)
	@Max(1)
	private Double				tax;

	@URL
	private String				link;

	private boolean				draftMode;

	// Derivated atributes --------------------------------------------------------------


	public Money totalAmount() {
		Double total = this.getQuantity().getAmount() + this.getTax() * this.getQuantity().getAmount();
		Money res = new Money();
		res.setAmount(total);
		res.setCurrency(this.getQuantity().getCurrency());
		return res;
	}

	// Relationships --------------------------------------------------------------------


	@NotNull
	@Valid
	@ManyToOne()
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Sponsorship sponsorship;

}
