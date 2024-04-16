
package acme.entities.systemcurrency;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SystemCurrency extends AbstractEntity {
	//Serialization indentifier --------------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	//Atributes ------------------------------------------------------------------------

	@NotBlank
	private String				currency;

	@NotBlank
	private String				acceptedCurrencies;
}
