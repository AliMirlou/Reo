package nl.cwi.reo.semantics.api;

import java.util.Map;

public interface IntegerExpression extends ValueExpression {
	
	public IntegerExpression evaluate(Map<String, Expression> params);
	
}
