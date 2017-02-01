package nl.cwi.reo.semantics.expressions;

import java.util.Map;

public interface BooleanExpression extends AtomicExpression {

	public BooleanExpression evaluate(Map<String, Expression> params);
	
}
