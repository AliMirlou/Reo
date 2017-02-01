package nl.cwi.reo.interpret.signatures;

import java.util.Map;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.variables.VariableExpression;
import nl.cwi.reo.semantics.Evaluable;
import nl.cwi.reo.semantics.expressions.Expression;

/**
 * An immutable parameter implementation.
 */
public final class Parameter implements Evaluable<Parameter> {
	
	private final VariableExpression var;
	
	private final ParameterType type;
	
	public Parameter(VariableExpression var, ParameterType type) {
		if (var == null || type == null)
			throw new NullPointerException();
		this.var = var;
		this.type = type;
	}

	public VariableExpression getVariable() {
		return this.var;
	}
	
	public ParameterType getType() {
		return this.type;
	}

	@Override
	public Parameter evaluate(Map<String, Expression> params) throws CompilationException {
		Expression e = var.evaluate(params);
		if (!(e instanceof VariableExpression))
			e = var;
		return new Parameter((VariableExpression)e, type);
	}
	
	@Override
	public String toString() {
		return var + (type instanceof TypeTag ? ":" : "") + type;
	}
}
