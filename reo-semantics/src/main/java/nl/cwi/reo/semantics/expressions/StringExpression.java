package nl.cwi.reo.semantics.expressions;

import java.util.Map;

public interface StringExpression extends AtomicExpression {

	public StringExpression evaluate(Map<String, Expression> params);
	
}
