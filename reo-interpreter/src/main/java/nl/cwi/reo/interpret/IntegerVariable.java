package nl.cwi.reo.interpret;

import java.util.Map;


public class IntegerVariable implements IntegerExpression {
	
	/**
	 * Variable name.
	 */
	private Variable var;
	
	/**
	 * Constructs a natural number from a string.
	 * @param s 	string representation of a natural number
	 */
	public IntegerVariable(Variable var) {
		if (var == null)
			throw new NullPointerException();
		this.var = var;
	}
	
	public Variable getVariable() {
		return var;
	}

	/**
	 * Evaluates this natural number to a Integer.
	 * @param params		 	parameter assignment
	 * @return Integer evaluation with respect to parameter assignment.
	 */
	@Override
	public IntegerExpression evaluate(Map<VariableName, Expression> params) throws Exception {
		Expression e = var.evaluate(params);
		if (e instanceof IntegerExpression) {
			return (IntegerExpression)e;
		} else if (e instanceof Variable) {
			return new IntegerVariable((Variable)e);
		} 
		return this;	
	}
	
	@Override
	public String toString() {
		return "" + var;
	}
}
