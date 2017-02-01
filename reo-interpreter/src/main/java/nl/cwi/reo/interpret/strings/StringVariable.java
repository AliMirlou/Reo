package nl.cwi.reo.interpret.strings;

import java.util.Map;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.variables.VariableExpression;
import nl.cwi.reo.semantics.expressions.Expression;
import nl.cwi.reo.semantics.expressions.StringExpression;

public class StringVariable implements StringExpression {

	/**
	 * Variable name.
	 */
	private VariableExpression var;

	/**
	 * Constructs a natural number from a string.
	 * @param s 	string representation of a natural number
	 */
	public StringVariable(VariableExpression var) {
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
	public StringExpression evaluate(Map<String, Expression> params) throws CompilationException {
		Expression e = var.evaluate(params);
		if (e instanceof StringExpression) {
			return (StringExpression)e;
		} else if (e instanceof VariableExpression) {
			return new StringVariable((VariableExpression)e);
		} 
		return this;
	}
	
	@Override
	public String toString() {
		return "" + var;
	}
}
