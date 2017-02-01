package nl.cwi.reo.interpret.components;

import java.util.Map;
import java.util.Objects;

import nl.cwi.reo.interpret.blocks.Body;
import nl.cwi.reo.interpret.expressions.ValueList;
import nl.cwi.reo.interpret.signatures.SignatureConcrete;
import nl.cwi.reo.interpret.signatures.SignatureExpression;
import nl.cwi.reo.interpret.variables.VariableList;
import nl.cwi.reo.semantics.api.Connector;
import nl.cwi.reo.semantics.api.Expression;
import nl.cwi.reo.semantics.api.Semantics;

public final class ComponentAtom<T extends Semantics<T>> implements ComponentDefinition<T> {
	
	/**
	 * Signature expression.
	 */
	private final SignatureExpression sign;
	
	/**
	 * Program.
	 */
	private final Body<T> prog;
	
	/**
	 * Constructs a new component value.
	 * @param sign
	 * @param prog
	 */
	public ComponentAtom(SignatureExpression sign, Body<T> prog) {
		if (sign == null || prog == null)
			throw new NullPointerException();
		this.sign = sign;
		this.prog = prog;
	}
	
	public SignatureExpression getSignature() {
		return sign;
	}
	
	public Connector<T> getInstances() {
		return prog.getConnector();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ComponentAtom<T> evaluate(Map<String, Expression> params) {
		return new ComponentAtom<T>(sign, prog.evaluate(params));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Body<T> instantiate(ValueList values, VariableList iface) {
		SignatureConcrete links = sign.evaluate(values, iface);
		Body<T> _prog = prog.instantiate(links);
		return _prog;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object other) {
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof ComponentAtom<?>)) return false;
	    ComponentAtom<?> p = (ComponentAtom<?>)other;
	   	return Objects.equals(this.sign, p.sign) && 
	   			Objects.equals(this.prog, p.prog);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
	    return Objects.hash(this.sign, this.prog);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return sign + "{" + prog + "}";
	}
}