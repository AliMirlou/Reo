package nl.cwi.reo.interpret.blocks;

import java.util.Iterator;
import java.util.Map;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.expressions.ExpressionList;
import nl.cwi.reo.interpret.semantics.Definitions;
import nl.cwi.reo.interpret.variables.VariableExpression;
import nl.cwi.reo.interpret.variables.Variable;
import nl.cwi.reo.interpret.variables.VariableList;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.semantics.connectors.Connector;
import nl.cwi.reo.semantics.expressions.Expression;

public final class Definition<T extends Semantics<T>> implements Statement<T> {

	private final VariableExpression var;
	
	private final Expression val;
	
	public Definition(VariableExpression var, Expression val) {
		if (var == null || val == null)
			throw new NullPointerException();
		this.var = var;
		this.val = val;
	}

	@Override
	public Statement<T> evaluate(Map<String, Expression> params) {
		
		Statement<T> prog = null;

		Expression e = var.evaluate(params);
		if (!(e instanceof VariableExpression))
			e = var;
		VariableExpression var_p = (VariableExpression)e;
		Expression val_p = val.evaluate(params);
		
		if (var_p instanceof Variable) {
			if (val_p instanceof Expression) {
				Definitions<T> definitions = new Definitions<T>();
				definitions.put(((Variable)var_p).getName(), (Expression)val_p);
				prog = new Body<T>(definitions, new Connector<T>());
			} else if (val_p instanceof ExpressionList) {
				throw new CompilationException(var.getToken(), "Value " + val_p + " must be of type expression.");	
			} 
		} else if (var_p instanceof VariableList) {	
			if (val_p instanceof ExpressionList) {	
				Definitions<T> definitions = new Definitions<T>();
				Iterator<Variable> var = ((VariableList) var_p).getList().iterator();
				Iterator<Expression> exp = ((ExpressionList)val_p).iterator();				
				while (var.hasNext() && exp.hasNext()) definitions.put(var.next().getName(), exp.next());
				prog = new Body<T>(definitions, new Connector<T>());
				
			} else if (val_p instanceof Expression) {
				throw new CompilationException(var.getToken(), "Value " + val_p + " must be of type list.");				
			}
		} else {
			prog = new Definition<T>(var_p, val_p);
		}
		
		return prog;
	}

	@Override
	public String toString() {
		return var + "=" + val;
	}
}
