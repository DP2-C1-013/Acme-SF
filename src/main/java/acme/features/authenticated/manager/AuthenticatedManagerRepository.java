
package acme.features.authenticated.manager;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.data.accounts.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.roles.Provider;

@Repository
public interface AuthenticatedManagerRepository extends AbstractRepository {

	@Query("SELECT m FROM Manager m where m.userAccount.id = :id")
	Provider findOneManagerByUserAccountId(int id);

	@Query("SELECT ua FROM UserAccount ua WHERE ua.id = :id")
	UserAccount findOneUserAccountById(int id);
}
