package nl.cwi.reo.interpret.blocks;

import nl.cwi.reo.semantics.api.Evaluable;
import nl.cwi.reo.semantics.api.Semantics;

public interface Block<T extends Semantics<T>> extends Evaluable<Block<T>> {
	
}
