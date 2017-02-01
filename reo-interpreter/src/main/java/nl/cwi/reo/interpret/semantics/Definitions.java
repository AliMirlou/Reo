package nl.cwi.reo.interpret.semantics;

import java.util.HashMap;
import java.util.Map;

import nl.cwi.reo.interpret.variables.Variable;
import nl.cwi.reo.semantics.api.Evaluable;
import nl.cwi.reo.semantics.api.Expression;
import nl.cwi.reo.semantics.api.Semantics;

public class Definitions<T extends Semantics<T>> extends HashMap<String, Expression> implements Evaluable<Definitions<T>> {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -6117381497904646504L;
	
	public Definitions() {}
	
	public Definitions(Map<String,? extends Expression> definitions) {
		super.putAll(definitions);
	}
	
	public Definitions<T> getUnifications() {
		Map<String, Expression> defns = new HashMap<String, Expression>();
		for (Map.Entry<String, Expression> def : super.entrySet()) 
			if (def.getValue() instanceof Variable)
				defns.put(def.getKey(), def.getValue());
		return new Definitions<T>(defns);
	}

	@Override
	public Definitions<T> evaluate(Map<String, Expression> params) {
		Map<String, Expression> defns_p = new HashMap<String, Expression>();
		for (Map.Entry<String, Expression> def : super.entrySet()) 
			defns_p.put(def.getKey(), def.getValue().evaluate(params));
		return new Definitions<T>(defns_p);
		// TODO Possibly local variables in this definition get instantiated by variables from the context.
	}
}
