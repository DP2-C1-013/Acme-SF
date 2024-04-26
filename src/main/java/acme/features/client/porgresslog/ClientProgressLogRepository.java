
package acme.features.client.porgresslog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.contract.Contract;
import acme.entities.progresslog.ProgressLog;

@Repository
public interface ClientProgressLogRepository extends AbstractRepository {

	@Query("SELECT c FROM Contract c WHERE c.id = :id")
	Contract findOneContractById(int id);

	@Query("SELECT c FROM ProgressLog c")
	Collection<ProgressLog> findAllProgressLogs();

	@Query("select pl from ProgressLog pl where pl.id = :id")
	ProgressLog findOneProgressLogById(int id);

	@Query("select pl from ProgressLog pl where pl.contract.id = :contractId")
	Collection<ProgressLog> findManyProgressLogByContractId(int contractId);

}
