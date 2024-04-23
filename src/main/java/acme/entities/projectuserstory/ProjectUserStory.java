
package acme.entities.projectuserstory;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import acme.client.data.AbstractEntity;
import acme.entities.project.Project;
import acme.entities.userstory.UserStory;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ProjectUserStory extends AbstractEntity {

	// Serialization indentifier --------------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Atributes ------------------------------------------------------------------------

	@ManyToOne(optional = false)
	@Valid
	@OnDelete(action = OnDeleteAction.CASCADE)
	@NotNull
	private Project				project;

	@ManyToOne(optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@Valid
	@NotNull
	private UserStory			userStory;
}
