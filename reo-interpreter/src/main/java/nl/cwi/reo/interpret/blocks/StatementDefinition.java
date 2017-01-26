package nl.cwi.reo.interpret.blocks;

import java.util.Iterator;
import java.util.Map;

import nl.cwi.reo.interpret.ranges.Range;
import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.ranges.ExpressionList;
import nl.cwi.reo.interpret.semantics.Definitions;
import nl.cwi.reo.interpret.semantics.InstanceList;
import nl.cwi.reo.interpret.variables.Variable;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.interpret.variables.VariableNameList;
import nl.cwi.reo.semantics.Semantics;

public final class StatementDefinition<T extends Semantics<T>> implements Statement<T> {

	private final Variable var;
	
	private final Range val;
	
	public StatementDefinition(Variable var, Range val) {
		if (var == null || val == null)
			throw new NullPointerException();
		this.var = var;
		this.val = val;
	}

	@Override
	public Statement<T> evaluate(Map<VariableName, Expression> params) throws Exception {
		
		Statement<T> prog = null;

		Range e = var.evaluate(params);
		if (!(e instanceof Variable))
			e = var;
		Variable var_p = (Variable)e;
		Range val_p = val.evaluate(params);
		
		if (var_p instanceof VariableName) {
			if (val_p instanceof Expression) {
				Definitions definitions = new Definitions();
				definitions.put((VariableName)var_p, (Expression)val_p);
				prog = new Program<T>(definitions, new InstanceList<T>());
			} else if (val_p instanceof ExpressionList) {
				throw new Exception("Value " + val_p + " must be of type expression.");	
			} 
		} else if (var_p instanceof VariableNameList) {	
			if (val_p instanceof ExpressionList) {	
				Definitions definitions = new Definitions();
				Iterator<VariableName> var = ((VariableNameList) var_p).getList().iterator();
				Iterator<Expression> exp = ((ExpressionList)val_p).iterator();				
				while (var.hasNext() && exp.hasNext()) definitions.put(var.next(), exp.next());
				prog = new Program<T>(definitions, new InstanceList<T>());
				
			} else if (val_p instanceof Expression) {
				throw new Exception("Value " + val_p + " must be of type list.");				
			}
		} else {
			prog = new StatementDefinition<T>(var_p, val_p);
		}
		
		return prog;
	}

	@Override
	public String toString() {
		return var + "=" + val;
	}
}
