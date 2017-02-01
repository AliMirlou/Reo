package nl.cwi.reo.interpret.components;

import java.util.Map;

import nl.cwi.reo.interpret.blocks.Block;
import nl.cwi.reo.interpret.expressions.ValueList;
import nl.cwi.reo.interpret.variables.VariableList;
import nl.cwi.reo.semantics.api.Expression;
import nl.cwi.reo.semantics.api.Semantics;
import nl.cwi.reo.semantics.api.ValueExpression;

public interface ComponentDefinition<T extends Semantics<T>> extends ValueExpression {

	/**
	 * Instantiates the parameters and nodes in the body of a component definition.
	 * @param values	parameter values
	 * @param iface		nodes in the interface
	 * @return The instantiated body of this definition, or null
	 */
	public Block<T> instantiate(ValueList values, VariableList iface);

	/**
	 * Substitutes (component) variables with (component) expressions.
	 * @param param			collection of known assignments.
	 * @return Component expression whose body is evaluated using known assignments.
	 * @throws Exception 
	 */
	public ComponentDefinition<T> evaluate(Map<String, Expression> params);
}
