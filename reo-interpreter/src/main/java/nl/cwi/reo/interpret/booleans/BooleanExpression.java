package nl.cwi.reo.interpret.booleans;

import java.util.Map;

import nl.cwi.reo.interpret.arrays.Expression;
import nl.cwi.reo.interpret.variables.VariableName;


public interface BooleanExpression extends Expression {

	public BooleanExpression evaluate(Map<VariableName, Expression> params) throws Exception;
	
}
