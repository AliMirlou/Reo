package nl.cwi.reo.interpret.signatures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.Token;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.expressions.Expressions;
import nl.cwi.reo.interpret.variables.VariableExpression;
import nl.cwi.reo.interpret.variables.Variable;
import nl.cwi.reo.interpret.variables.VariableList;
import nl.cwi.reo.semantics.expressions.Expression;

public final class InterfaceExpression extends ArrayList<InterfaceNode> implements Expressions {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -4878686718124263911L;
	
	/**
	 * Token
	 */
	private final Token token;
	
	/**
	 * Constructs an interface out of a list of variables.
	 * @param vars	list of variables (each referring to a node or node range)
	 */
	public InterfaceExpression(List<InterfaceNode> vars, Token token) {
		if (vars == null || token == null)
			throw new NullPointerException();
		for (InterfaceNode x : vars) {
			if (x == null)
				throw new NullPointerException();
			super.add(x);
		}
		this.token = token;
	}

	@Override
	public Expressions evaluate(Map<String, Expression> params) {
		List<Variable> list_p = new ArrayList<Variable>();
		for (InterfaceNode x : this) {
			Expression r = x.getVariable().evaluate(params);	
			if (r instanceof VariableList) {
				for (Variable v : ((VariableList)r).getList())
					list_p.add(v);
			} else if (r instanceof Variable) {
				list_p.add((Variable)r);				
			} else if (r instanceof VariableExpression) {
				return this;
			} else {
				throw new CompilationException(x.getVariable().getToken(), "Node variable " + x + " cannot be assigned to " + r);
			}
		}
		return new VariableList(list_p, token);
	}
	
	@Override
	public String toString() {
		String s = "(";
		Iterator<InterfaceNode> ifnode = this.iterator();
		while (ifnode.hasNext())
			s += ifnode.next() + (ifnode.hasNext() ? "," : "");
		return s + ")";
	}
}
