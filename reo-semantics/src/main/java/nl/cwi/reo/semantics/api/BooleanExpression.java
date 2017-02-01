package nl.cwi.reo.semantics.api;

import java.util.Map;

public interface BooleanExpression extends ValueExpression {

	public BooleanExpression evaluate(Map<String, Expression> params);
	
}
