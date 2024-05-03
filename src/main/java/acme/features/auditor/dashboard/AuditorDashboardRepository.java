
package acme.features.auditor.dashboard;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.codeaudit.CodeAudit;
import acme.entities.codeaudit.CodeAuditType;

@Repository
public interface AuditorDashboardRepository extends AbstractRepository {

	@Query("select c.type from CodeAudit c where c.auditor.id = :auditorId and c.draftMode = false")
	Collection<CodeAuditType> findPublishedCodeAuditsAsTypes(int auditorId);

	@Query("Select ca FROM CodeAudit ca WHERE ca.auditor.id = :auditorId")
	List<CodeAudit> findAllCodeAudits(int auditorId);

	@Query("SELECT COUNT(ca) FROM CodeAudit ca WHERE ca.type = 0 AND ca.auditor.id = :auditorId and draftMode = 0")
	Integer numStaticCodeAudits(int auditorId);

	@Query("SELECT COUNT(ca) FROM CodeAudit ca WHERE ca.type = 1 AND ca.auditor.id = :auditorId and ca.draftMode = 0")
	Integer numDynamicCodeAudits(int auditorId);

	@Query("select (select count(ar) from AuditRecord ar where ar.codeAudit.id = a.id) from CodeAudit a where a.auditor.id = :auditorId and a.draftMode = false")
	Collection<Double> auditingRecordsPerAudit(int auditorId);

	@Query("select max(select count(ar) from AuditRecord ar where ar.codeAudit.id = a.id) from CodeAudit a where a.auditor.id = :auditorId and a.draftMode = false")
	Integer maxAuditingRecords(int auditorId);

	@Query("select min(select count(ar) from AuditRecord ar where ar.codeAudit.id = a.id) from CodeAudit a where a.auditor.id = :auditorId and a.draftMode = false")
	Integer minAuditingRecords(int auditorId);

	@Query("select avg(select count(ar) from AuditRecord ar where ar.codeAudit.id = a.id) from CodeAudit a where a.auditor.id = :auditorId and a.draftMode = false")
	Double averageAuditingRecords(int auditorId);

	@Query("SELECT SQRT(AVG((SELECT COUNT(ar) FROM AuditRecord ar WHERE ar.codeAudit.id = a.id) * (SELECT COUNT(ar) FROM AuditRecord ar WHERE ar.codeAudit.id = a.id)) - AVG((SELECT COUNT(ar) FROM AuditRecord ar WHERE ar.codeAudit.id = a.id)) * AVG((SELECT COUNT(ar) FROM AuditRecord ar WHERE ar.codeAudit.id = a.id))) FROM CodeAudit a WHERE a.auditor.id = :auditorId AND a.draftMode = false")
	Double deviationAuditingRecords(int auditorId);

	@Query("select avg(time_to_sec(timediff(ar.endDate, ar.startDate)) / 3600) from AuditRecord ar where ar.codeAudit.auditor.id = :auditorId and ar.codeAudit.draftMode = false")
	Double averageRecordPeriod(int auditorId);

	@Query("select stddev(time_to_sec(timediff(ar.endDate, ar.startDate)) / 3600) from AuditRecord ar where ar.codeAudit.auditor.id = :auditorId and ar.codeAudit.draftMode = false")
	Double deviationRecordPeriod(int auditorId);

	@Query("SELECT MAX(FUNCTION('TIMESTAMPDIFF', SECOND, ar.startDate, ar.endDate) / 3600) " + "FROM AuditRecord ar " + "JOIN ar.codeAudit ca " + "WHERE ca.auditor.id = :auditorId AND ca.draftMode = false")
	Double maxRecordPeriod(int auditorId);

	@Query("select min(time_to_sec(timediff(ar.endDate, ar.startDate)) / 3600) from AuditRecord ar where ar.codeAudit.auditor.id = :auditorId and ar.codeAudit.draftMode = false")
	Double minRecordPeriod(int auditorId);

	@Query("select count(c) from CodeAudit c where c.auditor.id = :auditorId and c.draftMode = false")
	int totalCodeAudits(int auditorId);
}
