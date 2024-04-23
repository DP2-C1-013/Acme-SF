
package acme.features.sponsor.dashboard;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.systemcurrency.SystemCurrency;

@Repository
public interface SponsorDashboardRepository extends AbstractRepository {

	@Query("SELECT sc FROM SystemCurrency sc")
	List<SystemCurrency> findSystemCurrencies();

	@Query("SELECT COUNT(i) FROM Invoice i WHERE i.tax <= 0.21 and i.sponsorship.sponsor.id = :sponsorId")
	Integer numOfInvoicesLessThan21Tax(int sponsorId);

	@Query("SELECT COUNT(s) FROM Sponsorship s WHERE s.link != null and s.sponsor.id = :sponsorId")
	Integer numOfLinkedSponsorships(int sponsorId);

	@Query("SELECT AVG(s.amount.amount) FROM Sponsorship s WHERE s.sponsor.id = :sponsorId and s.amount.currency = :currency")
	Double averageAmountSponsorship(int sponsorId, String currency);

	@Query("SELECT STDDEV(s.amount.amount) FROM Sponsorship s WHERE s.sponsor.id = :sponsorId and s.amount.currency = :currency")
	Double deviationAmountSponsorship(int sponsorId, String currency);

	@Query("SELECT MIN(s.amount.amount) FROM Sponsorship s WHERE s.sponsor.id = :sponsorId and s.amount.currency = :currency")
	Double minAmountSponsorship(int sponsorId, String currency);

	@Query("SELECT MAX(s.amount.amount) FROM Sponsorship s WHERE s.sponsor.id = :sponsorId and s.amount.currency = :currency")
	Double maxAmountSponsorship(int sponsorId, String currency);

	@Query("SELECT AVG(i.quantity.amount) FROM Invoice i WHERE i.sponsorship.sponsor.id = :sponsorId and i.quantity.currency = :currency")
	Double averageQuantityInvoice(int sponsorId, String currency);

	@Query("SELECT STDDEV(i.quantity.amount) FROM Invoice i WHERE i.sponsorship.sponsor.id = :sponsorId and i.quantity.currency = :currency")
	Double deviationQuantityInvoice(int sponsorId, String currency);

	@Query("SELECT MIN(i.quantity.amount) FROM Invoice i WHERE i.sponsorship.sponsor.id = :sponsorId and i.quantity.currency = :currency")
	Double minQuantityInvoice(int sponsorId, String currency);

	@Query("SELECT MAX(i.quantity.amount) FROM Invoice i WHERE i.sponsorship.sponsor.id = :sponsorId and i.quantity.currency = :currency")
	Double maxQuantityInvoice(int sponsorId, String currency);

}
