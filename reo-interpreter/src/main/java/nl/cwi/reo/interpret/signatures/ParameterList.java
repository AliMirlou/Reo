package nl.cwi.reo.interpret.signatures;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.stringtemplate.v4.ST;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.variables.Variable;
import nl.cwi.reo.interpret.variables.VariableList;
import nl.cwi.reo.semantics.Evaluable;
import nl.cwi.reo.semantics.expressions.Expression;

public class ParameterList extends ArrayList<Parameter> implements Evaluable<ParameterList> {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 2066458682039126937L;

	public ParameterList() { }
	
	public ParameterList(List<Parameter> list) {
		if (list == null)
			throw new NullPointerException();
		for (Parameter x : list) {
			if (x == null) 
				throw new NullPointerException();
			super.add(x);
		}
	}

	@Override
	public ParameterList evaluate(Map<String, Expression> params) throws CompilationException {
		List<Parameter> list_p = new ArrayList<Parameter>();
		for (Parameter x : this) {
			Parameter x_p = x.evaluate(params);	
			if (x_p.getVariable() instanceof VariableList) {
				for (Variable v : ((VariableList)x_p.getVariable()).getList())
					list_p.add(new Parameter(v, x_p.getType()));
			} else {
				list_p.add(x_p);				
			}
		}
		return new ParameterList(list_p);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		List<String> params = new ArrayList<String>();
		for (Parameter p : this) 
			params.add(p.toString());
		ST st = new ST("<$params; separator=\", \"$>", '$', '$');
		st.add("params", params);
		return st.render();
	}
}