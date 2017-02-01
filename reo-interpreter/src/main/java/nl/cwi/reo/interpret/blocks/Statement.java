package nl.cwi.reo.interpret.blocks;

import nl.cwi.reo.semantics.Evaluable;
import nl.cwi.reo.semantics.Semantics;

public interface Statement<T extends Semantics<T>> extends Evaluable<Statement<T>> {
	
}
