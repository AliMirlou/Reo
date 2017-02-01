package nl.cwi.reo.interpret.strings;

import java.util.Map;

import nl.cwi.reo.semantics.api.Expression;
import nl.cwi.reo.semantics.api.ValueExpression;

public interface StringExpression extends ValueExpression {

	public StringExpression evaluate(Map<String, Expression> params);
	
}
