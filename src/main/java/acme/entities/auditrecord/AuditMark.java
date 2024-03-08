
package acme.entities.auditrecord;

public enum AuditMark {

	A_PLUS("A+"), A("A"), B("B"), C("C"), F("F"), F_MINUS("F-");


	private final String displayValue;


	private AuditMark(final String displayValue) {
		this.displayValue = displayValue;
	}

	@Override
	public String toString() {
		return this.displayValue;
	}
}
