package nl.cwi.reo.interpret.blocks;

import java.util.Map;

import nl.cwi.reo.interpret.components.ComponentDefinition;
import nl.cwi.reo.interpret.expressions.Expressions;
import nl.cwi.reo.interpret.expressions.ValueList;
import nl.cwi.reo.interpret.variables.VariableList;
import nl.cwi.reo.semantics.api.Expression;
import nl.cwi.reo.semantics.api.Semantics;

public final class Instance<T extends Semantics<T>> implements Block<T> {

	public final ComponentDefinition<T> cexpr;

	public final Expressions plist;
	
	private final Expressions iface;

	public Instance(ComponentDefinition<T> cexpr, Expressions plist, Expressions iface) {
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
	public Block<T> evaluate(Map<String, Expression> params) {
		ComponentDefinition<T> cexpr_p = cexpr.evaluate(params);
		Expressions plist_p = plist.evaluate(params); 
		Expressions iface_p = iface.evaluate(params); 
		if (plist_p instanceof ValueList && iface_p instanceof VariableList) {
			ValueList values = (ValueList)plist_p;
			VariableList nodes = (VariableList)iface_p;
			Block<T> e = cexpr_p.instantiate(values, nodes);
			if (e != null) 
				return e.evaluate(params);
		}
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