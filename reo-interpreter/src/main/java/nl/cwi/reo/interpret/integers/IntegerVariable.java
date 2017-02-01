package nl.cwi.reo.interpret.integers;

import java.util.Map;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.variables.VariableExpression;
import nl.cwi.reo.semantics.expressions.Expression;
import nl.cwi.reo.semantics.expressions.IntegerExpression;

public class IntegerVariable implements IntegerExpression {
	
	/**
	 * Variable name.
	 */
	private VariableExpression var;
	
	/**
	 * Constructs a natural number from a string.
	 * @param s 	string representation of a natural number
	 */
	public IntegerVariable(VariableExpression var) {
		if (var == null)
			throw new NullPointerException();
		this.var = var;
	}
	
	public VariableExpression getVariable() {
		return var;
	}

	/**
	 * Evaluates this natural number to a Integer.
	 * @param params		 	parameter assignment
	 * @return Integer evaluation with respect to parameter assignment.
	 */
	@Override
	public IntegerExpression evaluate(Map<String, Expression> params) throws CompilationException {
		Expression e = var.evaluate(params);
		if (e instanceof IntegerExpression) {
			return (IntegerExpression)e;
		} else if (e instanceof VariableExpression) {
			return new IntegerVariable((VariableExpression)e);
		} 
		return this;	
	}
	
	@Override
	public String toString() {
		return "" + var;
	}
}
