package nl.cwi.reo.interpret.signatures;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.stringtemplate.v4.ST;

import nl.cwi.reo.interpret.variables.Variable;
import nl.cwi.reo.interpret.variables.VariableList;
import nl.cwi.reo.semantics.Evaluable;
import nl.cwi.reo.semantics.expressions.Expression;

public class NodeList extends ArrayList<Node> implements Evaluable<NodeList> {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -8325490660234832292L;

	public NodeList() { }
	
	public NodeList(List<Node> list) {
		if (list == null)
			throw new NullPointerException();
		for (Node x : list) {
			if (x == null) 
				throw new NullPointerException();
			super.add(x);
		}
	}

	@Override
	public NodeList evaluate(Map<String, Expression> params) {
		List<Node> list_p = new ArrayList<Node>();
		for (Node x : this) {
			Node x_p = x.evaluate(params);	
			if (x_p.getVariable() instanceof VariableList) {
				for (Variable v : ((VariableList)x_p.getVariable()).getList())
					list_p.add(new Node(v, x_p.getNodeType(), x_p.getTypeTag()));
			} else {
				list_p.add(x_p);				
			}
		}
		return new NodeList(list_p);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		List<String> params = new ArrayList<String>();
		for (Node p : this) 
			params.add(p.toString());
		ST st = new ST("(<params; separator=\", \">)");
		st.add("params", params);
		return st.render();
	}
}
