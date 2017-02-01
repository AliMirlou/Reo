package nl.cwi.reo.semantics.api;

import static org.junit.Assert.*;
import org.junit.Test;

import nl.cwi.reo.semantics.Port;
import nl.cwi.reo.semantics.PortType;
import nl.cwi.reo.semantics.PrioType;

public class PortTest {
	
	@Test
	public void join_OriginalHasNoTypes() {
		Port origin = new Port("origin", PortType.NONE, PrioType.NONE, "", false);
		Port target = new Port("target", PortType.IN, PrioType.NONE, "int", false);
		Port composed = target.join(origin);
		
		assertEquals(composed.getType(), PortType.IN);
		assertEquals(composed.getPrioType(), PrioType.NONE);
		assertEquals(composed.getTypeTag(), "int");
	}
	
	@Test
	public void join_Inherit() {
		Port origin = new Port("origin", PortType.IN, PrioType.AMPERSANT, "string", false);
		Port target = new Port("target", PortType.OUT, PrioType.NONE, "int", false);
		Port composed = target.join(origin);
		
		assertEquals(composed.getType(), PortType.IN);
		assertEquals(composed.getPrioType(), PrioType.NONE);
		assertEquals(composed.getTypeTag(), "string");
	}

}
