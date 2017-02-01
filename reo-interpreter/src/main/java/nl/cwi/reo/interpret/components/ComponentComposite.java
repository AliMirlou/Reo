package nl.cwi.reo.interpret.components;

import java.util.Map;

import nl.cwi.reo.interpret.blocks.Body;
import nl.cwi.reo.interpret.blocks.Block;
import nl.cwi.reo.interpret.expressions.ValueList;
import nl.cwi.reo.interpret.signatures.SignatureConcrete;
import nl.cwi.reo.interpret.signatures.SignatureExpression;
import nl.cwi.reo.interpret.variables.VariableList;
import nl.cwi.reo.semantics.api.Expression;
import nl.cwi.reo.semantics.api.Semantics;

public class ComponentComposite<T extends Semantics<T>> implements ComponentDefinition<T> {
	
	private SignatureExpression sign;
	
	private Block<T> block;

	public ComponentComposite(SignatureExpression sign, Block<T> body) {
		if (sign == null || body == null)
			throw new NullPointerException();
		this.sign = sign;
		this.block = body;
	}
	
	public ComponentDefinition<T> evaluate(Map<String, Expression> params) {
		Block<T> prog = block.evaluate(params);
		if (prog instanceof Body)
			return new ComponentAtom<T>(sign, (Body<T>)prog);
		return new ComponentComposite<T>(sign, prog);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Block<T> instantiate(ValueList values, VariableList iface) {
		SignatureConcrete v = sign.evaluate(values, iface);
		Block<T> _body = block.evaluate(v.getDefinitions());
		
//		System.out.println("[info] " + "Body " + reoBlock + " evaluated in " + v.getDefinitions() + " gives \n\t" + _body);

		Block<T> out = null;
		
		if (_body instanceof Body) {

			System.out.println("[info] " + "Assembly: " + _body );
			
			out = ((Body<T>)_body).instantiate(v);

//			System.out.println("[info] " + this + " instantiated in " + values + iface + " gives " + out);
		}
		
		out = _body;
		
		return out;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "" + sign + block;
	}
}
