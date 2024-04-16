
package acme.features.client.contract;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.contract.Contract;
import acme.roles.client.Client;

@Repository
public interface ClientContractRepository extends AbstractRepository {

	@Query("SELECT cl FROM Client cl WHERE cl.userAccount.id = :id")
	Client findClientByUserAccountId(int id);

	@Query("SELECT c FROM Contract c WHERE c.id = :id")
	Contract findOneContractById(int id);

	@Query("SELECT DISTINCT c FROM Contract c WHERE c.client.id = :clientId")
	Collection<Contract> findContractsByClientId(int clientId);

}
