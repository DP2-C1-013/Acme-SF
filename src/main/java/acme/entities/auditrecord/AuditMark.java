
package acme.entities.auditrecord;

public enum AuditMark {

	A_PLUS("A+"), A("A"), B("B"), C("C"), F("F"), F_MINUS("F-");


	private final String displayValue;


	private AuditMark(final String displayValue) {
		this.displayValue = displayValue;
	}

	public static AuditMark parseAuditMark(final String value) {
		for (AuditMark mark : AuditMark.values())
			if (mark.displayValue.equals(value))
				return mark;
		throw new IllegalArgumentException("Invalid AuditMark value: " + value);
	}

	@Override
	public String toString() {
		return this.displayValue;
	}
}
