
package acme.features.sponsor.sponsorship;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.invoice.Invoice;
import acme.entities.project.Project;
import acme.entities.sponsorship.Sponsorship;
import acme.entities.systemconfiguration.SystemConfiguration;
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

	@Query("SELECT DISTINCT p FROM Project p WHERE p.draftMode = false")
	Collection<Project> findAllProjectsDraftModeFalse();

	@Query("SELECT p FROM Project p WHERE p.code = :code")
	Project findOneProjectByCode(String code);

	@Query("SELECT COALESCE(SUM((i.quantity.amount + i.tax * i.quantity.amount)), -1) FROM Invoice i WHERE i.sponsorship.id = :id and i.draftMode = false")
	Double findSumAmountInvoicesBySponsorshipId(int id);

	@Query("SELECT DISTINCT i FROM Invoice i WHERE i.sponsorship.id = :id")
	Collection<Invoice> findManyInvoicesBySponsorshipId(int id);

	@Query("SELECT COUNT(i) FROM Invoice i WHERE i.sponsorship.id = :id and i.draftMode = false")
	Integer findNumberPublishedInvoicesBySponsorshipId(int id);

	@Query("SELECT DISTINCT i.quantity.currency FROM Invoice i WHERE i.sponsorship.id = :id")
	Collection<String> findManyCurrenciesInInvoiceBySponsorshipId(int id);

	@Query("SELECT sc FROM SystemConfiguration sc")
	List<SystemConfiguration> findSystemCurrencies();
}
