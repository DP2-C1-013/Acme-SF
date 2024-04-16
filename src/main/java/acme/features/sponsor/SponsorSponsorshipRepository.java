
package acme.features.sponsor;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.project.Project;
import acme.entities.sponsorship.Sponsorship;
import acme.roles.Sponsor;

@Repository
public interface SponsorSponsorshipRepository extends AbstractRepository {

	@Query("SELECT DISTINCT s FROM Sponsorship s where s.sponsor.id = :sponsorId")
	Collection<Sponsorship> findCreatedSponsorshipsBySponsorId(int sponsorId);

	@Query("SELECT s FROM Sponsor s WHERE s.id = :id")
	Sponsor findOneSponsorBySponsorId(int id);

	@Query("SELECT s FROM Sponsorship s WHERE s.id = :id")
	Sponsorship findOneSponsorshipById(int id);

	@Query("SELECT s FROM Sponsorship s WHERE s.code = :code")
	Sponsorship findOneSponsorshipByCode(String code);

	@Query("SELECT DISTINCT p FROM Project p")
	Collection<Project> findAllProjects();

	@Query("SELECT DISTINCT p FROM Project p WHERE p.draftMode = true")
	Collection<Project> findAllProjectsDraftModeTrue();

	@Query("SELECT p FROM Project p WHERE p.code = :code")
	Project findOneProjectByCode(String code);
}
