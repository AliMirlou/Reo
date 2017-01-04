package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NodeList implements Evaluable<NodeList> {
	
	private final List<Node> list;
	
	public NodeList() {
		this.list = Collections.unmodifiableList(new ArrayList<Node>());
	}
	
	public NodeList(List<Node> list) {
		if (list == null)
			throw new IllegalArgumentException("Argument cannot be null.");
		this.list = Collections.unmodifiableList(list);
	}

	public List<Node> getList() {
		return list;
	}

	@Override
	public NodeList evaluate(Map<VariableName, Expression> params) throws Exception {
		List<Node> list_p = new ArrayList<Node>();
		for (Node x : list) {
			Node x_p = x.evaluate(params);
			List<Node> x_p_list = x_p.getList();
			if (x_p_list != null) {
				list_p.addAll(x_p_list);
			} else {
				list_p.add(x_p);
			}
		}
		return new NodeList(list_p);
	}
	
	@Override
	public String toString() {
		String s = "(";
		Iterator<Node> x = list.iterator();
		while (x.hasNext())
			s += x.next() + (x.hasNext() ? "," : "");
		return s + ")";
	}
}
