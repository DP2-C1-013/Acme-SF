
package acme.datatypes;

import javax.persistence.Embeddable;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import acme.client.data.AbstractDatatype;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Money extends AbstractDatatype {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Digits(integer = 10, fraction = 2)
	@NotNull
	@Min(0)
	private Double				amount;

	@NotBlank
	private String				currency;

	// Object interface -------------------------------------------------------


	@Override
	public String toString() {
		StringBuilder result;

		result = new StringBuilder();
		result.append("<<");
		result.append(this.currency);
		result.append(" ");
		result.append(String.format("%.2f", this.amount));
		result.append(">>");

		return result.toString();
	}
}
