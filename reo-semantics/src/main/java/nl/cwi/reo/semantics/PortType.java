package nl.cwi.reo.semantics;

public enum PortType {
	IN, OUT, NONE;
	
	@Override
	public String toString() {
		switch(this) {
		case IN: return "?";
		case OUT: return "!";
		case NONE: return "";
		default: return null;
		}
	}
}
