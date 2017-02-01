package nl.cwi.reo.interpret.components;

import java.util.Map;

import nl.cwi.reo.interpret.blocks.Statement;
import nl.cwi.reo.interpret.expressions.ValueList;
import nl.cwi.reo.interpret.variables.VariableExpression;
import nl.cwi.reo.interpret.variables.VariableList;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.semantics.expressions.Expression;

public class ComponentVariable<T extends Semantics<T>> implements ComponentExpression<T> {
	
	private VariableExpression var;
	
	public ComponentVariable(VariableExpression var) {
		if (var == null)
			throw new NullPointerException();
		this.var = var;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Statement<T> instantiate(ValueList values, VariableList iface) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ComponentExpression<T> evaluate(Map<String, Expression> params) {
		Expression e = var.evaluate(params);
		if (e instanceof ComponentExpression)
			return (ComponentExpression<T>)e;
		else if (e instanceof VariableExpression)
			return new ComponentVariable<T>((VariableExpression)e);
		return this;	
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "" + var;
	}
}
