package nl.cwi.reo.interpret.expressions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nl.cwi.reo.semantics.expressions.Expression;
import nl.cwi.reo.semantics.expressions.AtomicExpression;

public class ValueList extends ArrayList<AtomicExpression> implements Expressions {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -2252873175064572188L;
	
	public ValueList() { }
	
	public ValueList(List<AtomicExpression> entries) {
		if (entries == null)
			throw new NullPointerException();
		for (AtomicExpression e : entries) {
			if (e == null)
				throw new NullPointerException();
			super.add(e);
		}
	}

	@Override
	public Expressions evaluate(Map<String, Expression> params) {
		List<AtomicExpression> entries = new ArrayList<AtomicExpression>();
		for (AtomicExpression e : this)
			entries.add(e.evaluate(params));
		return new ValueList(entries);
	}
	
	@Override
	public String toString() {
		String s = "<";
		Iterator<AtomicExpression> expr = this.iterator();
		while (expr.hasNext())
			s += expr.next() + (expr.hasNext() ? ", " : "" );
		return s + ">";
	}
}
