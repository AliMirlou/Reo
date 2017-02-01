package nl.cwi.reo.semantics;

import java.util.Map;

import nl.cwi.reo.semantics.expressions.Expression;

/**
 * A class implementing this interface can be evaluated in a set of parameters.
 * Each parameter value is given as a key-value pair containing the name of the 
 * parameter and the its value, respectively. Parameter values can itself be an 
 * expression.
 * 
 * @param <T> type of returned object after evaluation
 * @see Expression
 */
public interface Evaluable<T> {
	
	/**
	 * Substitutes free variables  by an expression.
	 * @param params			assignment of expressions to variables
	 * @return new expression with variables substituted.
	 */
	public T evaluate(Map<String, Expression> params);
}
