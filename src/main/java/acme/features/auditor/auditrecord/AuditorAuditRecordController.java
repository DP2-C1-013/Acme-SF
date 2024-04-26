
package acme.features.auditor.auditrecord;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.auditrecord.AuditRecord;
import acme.roles.Auditor;

@Controller
public class AuditorAuditRecordController extends AbstractController<Auditor, AuditRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorAuditRecordListService	listService;

	@Autowired
	private AuditorAuditRecordShowService	showService;

	/*
	 * @Autowired
	 * private SponsorInvoiceCreateService createService;
	 * 
	 * @Autowired
	 * private SponsorInvoiceDeleteService deleteService;
	 * 
	 * @Autowired
	 * private SponsorInvoiceUpdateService updateService;
	 * 
	 * @Autowired
	 * private SponsorInvoicePublishService publishService;
	 */

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);

		super.addBasicCommand("show", this.showService);
		//super.addBasicCommand("create", this.createService);
		/*
		 * super.addBasicCommand("delete", this.deleteService);
		 * super.addBasicCommand("update", this.updateService);
		 * 
		 * super.addCustomCommand("publish", "update", this.publishService);
		 */
	}

}
