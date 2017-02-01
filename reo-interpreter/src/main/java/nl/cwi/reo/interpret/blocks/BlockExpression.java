package nl.cwi.reo.interpret.blocks;

import java.util.Map;

import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.semantics.expressions.Expression;

public interface BlockExpression<T extends Semantics<T>> extends Statement<T> {
	
	/**
	 * Evaluates this block expression by assigning expressions to
	 * free variables.
	 * @param params			assignment of expressions to variables
	 * @return new expression with variables substituted.
	 */
	public BlockExpression<T> evaluate(Map<String, Expression> params);

}
