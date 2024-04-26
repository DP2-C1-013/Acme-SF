
package acme.features.client.contract;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.contract.Contract;
import acme.entities.progresslog.ProgressLog;
import acme.entities.project.Project;
import acme.roles.client.Client;

@Repository
public interface ClientContractRepository extends AbstractRepository {

	@Query("SELECT cl FROM Client cl WHERE cl.userAccount.id = :id")
	Client findClientByUserAccountId(int id);

	@Query("SELECT cl FROM Client cl WHERE cl.id = :id")
	Client findOneClientById(int id);

	@Query("SELECT c FROM Contract c WHERE c.id = :id")
	Contract findOneContractById(int id);

	@Query("SELECT c FROM Contract c WHERE c.code = :code")
	Contract findOneContractByCode(String code);

	@Query("SELECT p FROM Project p WHERE p.code = :code")
	Project findOneProjectByCode(String code);

	@Query("SELECT DISTINCT p FROM Project p WHERE p.draftMode = true")
	Collection<Project> findAllProjectsDraftModeTrue();

	@Query("SELECT DISTINCT p FROM Project p")
	Collection<Project> findAllProjects();

	@Query("SELECT DISTINCT c FROM Contract c WHERE c.client.id = :clientId")
	Collection<Contract> findContractsByClientId(int clientId);

	@Query("select pl from ProgressLog pl where pl.contract.id = :contractId")
	Collection<ProgressLog> findManyProgressLogsByContractId(int contractId);

	@Query("select distinct c from Contract c where c.project.id = :projectId")
	Collection<Contract> findManyContractByProjectId(int projectId);

}
