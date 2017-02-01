package nl.cwi.reo.semantics.expressions;

import java.util.Map;

public interface IntegerExpression extends AtomicExpression {
	
	public IntegerExpression evaluate(Map<String, Expression> params);
	
}
