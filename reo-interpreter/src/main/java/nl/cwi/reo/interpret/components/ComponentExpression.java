package nl.cwi.reo.interpret.components;

import java.util.Map;

import nl.cwi.reo.interpret.blocks.Statement;
import nl.cwi.reo.interpret.expressions.ValueList;
import nl.cwi.reo.interpret.variables.VariableList;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.semantics.expressions.Expression;
import nl.cwi.reo.semantics.expressions.AtomicExpression;

public interface ComponentExpression<T extends Semantics<T>> extends AtomicExpression {

	/**
	 * Instantiates the parameters and nodes in the body of a component definition.
	 * @param values	parameter values
	 * @param iface		nodes in the interface
	 * @return The instantiated body of this definition, or null
	 */
	public Statement<T> instantiate(ValueList values, VariableList iface);

	/**
	 * Substitutes (component) variables with (component) expressions.
	 * @param param			collection of known assignments.
	 * @return Component expression whose body is evaluated using known assignments.
	 * @throws Exception 
	 */
	public ComponentExpression<T> evaluate(Map<String, Expression> params);
}
