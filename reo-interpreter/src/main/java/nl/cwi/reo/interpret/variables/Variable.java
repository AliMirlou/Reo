package nl.cwi.reo.interpret.variables;

import java.util.Map;
import java.util.Objects;

import org.antlr.v4.runtime.Token;

import nl.cwi.reo.semantics.api.Expression;
import nl.cwi.reo.semantics.api.ValueExpression;

/**
 * An immutable implementation of a variable name.
 */
public final class Variable implements VariableExpression, ValueExpression {

	private final String name;
	
	private final Token token;
	
	public Variable(String name, Token token) {
		if (name == null)
			throw new NullPointerException();
		this.name = name;
		this.token = token;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public Token getToken() {
		return token;
	}
	
	public Variable addPrefix(String prefix) {
		return new Variable(prefix + name, token);
	}

	@Override
	public ValueExpression evaluate(Map<String, Expression> params) {
		Expression e = params.get(name);
		if (e instanceof ValueExpression) 
			return (ValueExpression)e;
		return this;
	}
	
	@Override
	public boolean equals(Object other) {
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof Variable)) return false;
	    Variable p = (Variable)other;
	   	return Objects.equals(this.name, p.name);
	}
	
	@Override
	public int hashCode() {
	    return Objects.hash(this.name);
	}
	
	@Override
	public String toString() {
		return name;
	}
}
