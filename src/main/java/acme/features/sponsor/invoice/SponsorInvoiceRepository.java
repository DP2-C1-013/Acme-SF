
package acme.features.sponsor.invoice;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.invoice.Invoice;
import acme.entities.sponsorship.Sponsorship;
import acme.entities.systemconfiguration.SystemConfiguration;

@Repository
public interface SponsorInvoiceRepository extends AbstractRepository {

	@Query("SELECT i FROM Invoice i WHERE i.sponsorship.id = :id")
	Collection<Invoice> findManyInvoicesBySponsorshipId(int id);

	@Query("SELECT i FROM Invoice i WHERE i.id = :id")
	Invoice findOneInvoiceById(int id);

	@Query("SELECT s FROM Sponsorship s")
	Collection<Sponsorship> findAllSponsorships();

	@Query("SELECT s FROM Sponsorship s WHERE s.id = :id")
	Sponsorship findOneSponsorshipById(int id);

	@Query("SELECT i FROM Invoice i WHERE i.code = :code")
	Invoice findOneInvoiceByCode(String code);

	@Query("SELECT COALESCE(SUM((i.quantity.amount + i.tax * i.quantity.amount)), 0.) FROM Invoice i WHERE i.sponsorship.id = :id and i.draftMode = false")
	Double findSumTotalAmountBySponsorshipId(int id);

	@Query("SELECT sc FROM SystemConfiguration sc")
	List<SystemConfiguration> findSystemCurrencies();
}
