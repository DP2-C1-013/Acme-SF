
package acme.features.auditor.auditrecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.auditrecord.AuditRecord;
import acme.entities.codeaudit.CodeAudit;

@Repository
public interface AuditorAuditRecordRepository extends AbstractRepository {

	@Query("SELECT DISTINCT c FROM CodeAudit c where c.auditor.id = :auditorId")
	Collection<CodeAudit> findCodeAuditByAuditor(int auditorId);

	@Query("SELECT ar FROM AuditRecord ar WHERE ar.codeAudit.id = :id")
	Collection<AuditRecord> findManyAuditRecordsByCodeAuditId(int id);

	@Query("SELECT ar FROM AuditRecord ar WHERE ar.id = :id")
	AuditRecord findOneAuditRecordById(int id);

	@Query("SELECT ar FROM AuditRecord ar WHERE ar.code = :code")
	AuditRecord findOneAuditRecordByCode(String code);

	@Query("SELECT c FROM CodeAudit c WHERE c.id = :id")
	CodeAudit findOneCodeAuditById(int id);
}
