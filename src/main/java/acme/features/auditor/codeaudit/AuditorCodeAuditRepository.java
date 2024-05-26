
package acme.features.auditor.codeaudit;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.auditrecord.AuditMark;
import acme.entities.auditrecord.AuditRecord;
import acme.entities.codeaudit.CodeAudit;
import acme.entities.project.Project;
import acme.roles.Auditor;

@Repository
public interface AuditorCodeAuditRepository extends AbstractRepository {

	@Query("SELECT DISTINCT c FROM CodeAudit c where c.auditor.id = :auditorId")
	Collection<CodeAudit> findCodeAuditByAuditor(int auditorId);

	@Query("SELECT c FROM CodeAudit c WHERE c.id = :id")
	CodeAudit findOneCodeAuditById(int id);

	@Query("SELECT DISTINCT p FROM Project p")
	Collection<Project> findAllProjects();

	@Query("SELECT c FROM Auditor c WHERE c.id = :id")
	Auditor findOneAuditorByAuditorId(int id);

	@Query("SELECT p FROM Project p WHERE p.code = :code")
	Project findOneProjectByCode(String code);

	@Query("SELECT c FROM CodeAudit c WHERE c.code = :code")
	CodeAudit findOneCodeAuditByCode(String code);

	@Query("SELECT DISTINCT p FROM Project p WHERE p.draftMode = false")
	Collection<Project> findAllProjectsDraftModeFalse();

	@Query("SELECT ar.mark FROM AuditRecord ar WHERE ar.codeAudit.id = :codeAuditId and ar.draftMode = 0 GROUP BY ar.mark ORDER BY COUNT(ar.mark) ASC")
	List<AuditMark> findMarkModeByCodeAudit(int codeAuditId);

	@Query("SELECT DISTINCT ar FROM AuditRecord ar WHERE ar.codeAudit.id = :id")
	Collection<AuditRecord> findManyAuditRecordsByCodeAuditId(int id);

	@Query("SELECT DISTINCT ar FROM AuditRecord ar WHERE ar.codeAudit.id = :id and ar.draftMode = 1")
	List<AuditRecord> findAuditRecordsDraftModeByCodeAuditId(int id);

	@Query("SELECT MIN(ar.startDate) FROM AuditRecord ar WHERE ar.codeAudit.id = :codeAuditId")
	Date findEarliestStartDateByCodeAuditId(int codeAuditId);

	@Query("SELECT COUNT(ar) FROM AuditRecord ar WHERE ar.codeAudit.id = :id and ar.draftMode = false")
	Integer findNumberPublishedAuditRecordsByCodeAuditId(int id);

}
