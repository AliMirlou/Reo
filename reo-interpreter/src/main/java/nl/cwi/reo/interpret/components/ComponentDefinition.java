package nl.cwi.reo.interpret.components;

import java.util.Map;
import java.util.Objects;

import org.stringtemplate.v4.ST;

import nl.cwi.reo.interpret.blocks.BlockExpression;
import nl.cwi.reo.interpret.blocks.Body;
import nl.cwi.reo.interpret.blocks.Instance;
import nl.cwi.reo.interpret.blocks.Statement;
import nl.cwi.reo.interpret.expressions.ValueList;
import nl.cwi.reo.interpret.signatures.SignatureConcrete;
import nl.cwi.reo.interpret.signatures.SignatureExpression;
import nl.cwi.reo.interpret.variables.VariableList;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.semantics.expressions.Expression;

public class ComponentDefinition<T extends Semantics<T>> implements ComponentExpression<T> {
	
	private SignatureExpression sign;
	
	private BlockExpression<T> block;

	public ComponentDefinition(SignatureExpression sign, BlockExpression<T> body) {
		if (sign == null || body == null)
			throw new NullPointerException();
		this.sign = sign;
		this.block = body;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ComponentExpression<T> evaluate(Map<String, Expression> params) {
		return new ComponentDefinition<T>(sign, block.evaluate(params));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Statement<T> instantiate(ValueList values, VariableList iface) {
		SignatureConcrete v = sign.evaluate(values, iface);
		BlockExpression<T> _body = block.evaluate(v.getDefinitions());
		if (_body instanceof Body) 		
			return ((Body<T>)_body).instantiate(v);
		return new Instance<T>(this, values, iface);		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object other) {
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof ComponentDefinition<?>)) return false;
	    ComponentDefinition<?> p = (ComponentDefinition<?>)other;
	   	return Objects.equals(this.sign, p.sign) && 
	   			Objects.equals(this.block, p.block);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
	    return Objects.hash(this.sign, this.block);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		ST st = new ST("<sign> <block>");
		st.add("sign", sign);
		st.add("block", block);
		return st.render();
	}
}
