package nl.cwi.reo.interpret.blocks;

import java.util.Map;

import nl.cwi.reo.interpret.components.ComponentExpression;
import nl.cwi.reo.interpret.expressions.Expressions;
import nl.cwi.reo.interpret.expressions.ValueList;
import nl.cwi.reo.interpret.variables.VariableList;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.semantics.expressions.Expression;

public final class Instance<T extends Semantics<T>> implements Statement<T> {

	public final ComponentExpression<T> cexpr;

	public final Expressions plist;
	
	private final Expressions iface;

	public Instance(ComponentExpression<T> cexpr, Expressions plist, Expressions iface) {
		if (cexpr == null || plist == null || iface == null)
			throw new NullPointerException();		
		this.cexpr = cexpr;
		this.plist = plist;
		this.iface = iface;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Statement<T> evaluate(Map<String, Expression> params) {
		ComponentExpression<T> cexpr_p = cexpr.evaluate(params);
		Expressions plist_p = plist.evaluate(params); 
		Expressions iface_p = iface.evaluate(params); 
		if (plist_p instanceof ValueList && iface_p instanceof VariableList)
			return cexpr_p.instantiate((ValueList)plist_p, (VariableList)iface_p);
		return new Instance<T>(cexpr_p, plist_p, iface_p);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "" + cexpr + plist + iface;
	}
}