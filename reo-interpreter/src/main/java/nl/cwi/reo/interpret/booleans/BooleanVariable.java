package nl.cwi.reo.interpret.booleans;

import java.util.Map;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.variables.VariableExpression;
import nl.cwi.reo.semantics.api.BooleanExpression;
import nl.cwi.reo.semantics.api.Expression;

public class BooleanVariable implements BooleanExpression {

	/**
	 * Variable name.
	 */
	private VariableExpression var;

	/**
	 * Constructs a natural number from a string.
	 * @param s 	string representation of a natural number
	 */
	public BooleanVariable(VariableExpression var) {
		if (var == null)
			throw new NullPointerException();
		this.var = var;
	}
	
	/**
	 * Evaluates this natural number to a Integer.
	 * @param params		 	parameter assignment
	 * @return Integer evaluation with respect to parameter assignment.
	 */
	@Override
	public BooleanExpression evaluate(Map<String, Expression> params) throws CompilationException {
		Expression e = var.evaluate(params);
		if (e instanceof BooleanExpression) {
			return (BooleanExpression)e;
		} else if (e instanceof VariableExpression) {
			return new BooleanVariable((VariableExpression)e);
		} 
		return this;
	}
	
	@Override
	public String toString() {
		return "" + var;
	}
}
