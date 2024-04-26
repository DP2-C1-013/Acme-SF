
package acme.features.auditor.dashboard;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.codeaudit.CodeAuditType;

@Repository
public interface AuditorDashboardRepository extends AbstractRepository {

	@Query("select c.type from CodeAudit c where c.auditor.id = :auditorId and c.draftMode = false")
	Collection<CodeAuditType> findPublishedCodeAuditsAsTypes(int auditorId);

	@Query("SELECT COUNT(ca) FROM CodeAudit ca WHERE ca.type = 'static' AND ca.auditor.id = :auditorId")
	Integer numStaticCodeAudits(int auditorId);

	@Query("SELECT COUNT(ca) FROM CodeAudit ca WHERE ca.type = 'dynamic' AND ca.auditor.id = :auditorId")
	Integer numDynamicCodeAudits(int auditorId);

	@Query("select (select count(ar) from AuditRecord ar where ar.codeAudit.id = a.id) from CodeAudit a where a.auditor.id = :auditorId and a.draftMode = false")
	Collection<Double> auditingRecordsPerAudit(int auditorId);

	@Query("select max(select count(ar) from AuditRecord ar where ar.codeAudit.id = a.id) from CodeAudit a where a.auditor.id = :auditorId and a.draftMode = false")
	Integer maxAuditingRecords(int auditorId);

	@Query("select min(select count(ar) from AuditRecord ar where ar.codeAudit.id = a.id) from CodeAudit a where a.auditor.id = :auditorId and a.draftMode = false")
	Integer minAuditingRecords(int auditorId);

	@Query("select avg(select count(ar) from AuditRecord ar where ar.codeAudit.id = a.id) from CodeAudit a where a.auditor.id = :auditorId and a.draftMode = false")
	Double averageAuditingRecords(int auditorId);

	//@Query("select stddev(select count(ar) from AuditRecord ar where ar.codeAudit.id = a.id) from CodeAudit a where a.auditor.id = :auditorId and a.draftMode = false")
	//Double deviationAuditingRecords(int auditorId);

	@Query("select avg(time_to_sec(timediff(ar.endDate, ar.startDate)) / 3600) from AuditRecord ar where ar.codeAudit.auditor.id = :auditorId and ar.codeAudit.draftMode = false")
	Double averageRecordPeriod(int auditorId);

	@Query("select stddev(time_to_sec(timediff(ar.endDate, ar.startDate)) / 3600) from AuditRecord ar where ar.codeAudit.auditor.id = :auditorId and ar.codeAudit.draftMode = false")
	Double deviationRecordPeriod(int auditorId);

	@Query("select max(time_to_sec(timediff(ar.endDate, ar.startDate)) / 3600) from AuditRecord ar where ar.codeAudit.auditor.id = :auditorId and ar.codeAudit.draftMode = false")
	Double maxRecordPeriod(int auditorId);

	@Query("select min(time_to_sec(timediff(ar.endDate, ar.startDate)) / 3600) from AuditRecord ar where ar.codeAudit.auditor.id = :auditorId and ar.codeAudit.draftMode = false")
	Double minRecordPeriod(int auditorId);

	@Query("select count(c) from CodeAudit c where c.auditor.id = :auditorId and c.draftMode = false")
	int totalCodeAudits(int auditorId);
}
