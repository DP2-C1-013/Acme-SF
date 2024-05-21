
package acme.features.any.auditor;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.codeaudit.CodeAudit;

@Repository
public interface AnyCodeAuditRepository extends AbstractRepository {

	@Query("SELECT s FROM CodeAudit s WHERE s.draftMode = false")
	Collection<CodeAudit> findPublishedCodeAudits();

	@Query("SELECT s FROM CodeAudit s WHERE s.id = :id")
	CodeAudit findOneCodeAuditById(int id);

}
