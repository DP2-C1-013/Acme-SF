
package acme.features.sponsor.dashboard;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.systemconfiguration.SystemConfiguration;

@Repository
public interface SponsorDashboardRepository extends AbstractRepository {

	@Query("SELECT sc FROM SystemConfiguration sc")
	List<SystemConfiguration> findSystemCurrencies();

	@Query("SELECT COUNT(i) FROM Invoice i WHERE i.tax <= 0.21 and i.sponsorship.sponsor.id = :sponsorId and i.draftMode = false")
	Integer numOfInvoicesLessThan21Tax(int sponsorId);

	@Query("SELECT COUNT(s) FROM Sponsorship s WHERE s.link != null and s.sponsor.id = :sponsorId and s.draftMode = false")
	Integer numOfLinkedSponsorships(int sponsorId);

	@Query("SELECT AVG(s.amount.amount) FROM Sponsorship s WHERE s.sponsor.id = :sponsorId and s.amount.currency = :currency and s.draftMode = false")
	Double averageAmountSponsorship(int sponsorId, String currency);

	@Query("SELECT STDDEV(s.amount.amount) FROM Sponsorship s WHERE s.sponsor.id = :sponsorId and s.amount.currency = :currency and s.draftMode = false")
	Double deviationAmountSponsorship(int sponsorId, String currency);

	@Query("SELECT MIN(s.amount.amount) FROM Sponsorship s WHERE s.sponsor.id = :sponsorId and s.amount.currency = :currency and s.draftMode = false")
	Double minAmountSponsorship(int sponsorId, String currency);

	@Query("SELECT MAX(s.amount.amount) FROM Sponsorship s WHERE s.sponsor.id = :sponsorId and s.amount.currency = :currency and s.draftMode = false")
	Double maxAmountSponsorship(int sponsorId, String currency);

	@Query("SELECT AVG(i.quantity.amount) FROM Invoice i WHERE i.sponsorship.sponsor.id = :sponsorId and i.quantity.currency = :currency and i.draftMode = false")
	Double averageQuantityInvoice(int sponsorId, String currency);

	@Query("SELECT STDDEV(i.quantity.amount) FROM Invoice i WHERE i.sponsorship.sponsor.id = :sponsorId and i.quantity.currency = :currency and i.draftMode = false")
	Double deviationQuantityInvoice(int sponsorId, String currency);

	@Query("SELECT MIN(i.quantity.amount) FROM Invoice i WHERE i.sponsorship.sponsor.id = :sponsorId and i.quantity.currency = :currency and i.draftMode = false")
	Double minQuantityInvoice(int sponsorId, String currency);

	@Query("SELECT MAX(i.quantity.amount) FROM Invoice i WHERE i.sponsorship.sponsor.id = :sponsorId and i.quantity.currency = :currency and i.draftMode = false")
	Double maxQuantityInvoice(int sponsorId, String currency);

}
