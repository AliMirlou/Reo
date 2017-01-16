package nl.cwi.reo.interpret.signatures;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.ranges.ExpressionList;
import nl.cwi.reo.interpret.semantics.Definitions;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.interpret.variables.VariableNameList;
import nl.cwi.reo.interpret.variables.VariableRange;
import nl.cwi.reo.semantics.Port;

public final class SignatureExpression implements ParameterType {
	
	// TODO allow a signature of the form <x[1..k]>() to be instantiated with an arbitrary
	// number of paramter values, and assign their cardinality to k.
	
	private final ParameterList params;
	
	private final NodeList nodes;
	
	public SignatureExpression() {
		this.params = new ParameterList();
		this.nodes = new NodeList();
	}
	
	public SignatureExpression(ParameterList params, NodeList nodes) {
		if (params == null || nodes == null)
			throw new NullPointerException();
		this.params = params;
		this.nodes = nodes;
	}
	
	public SignatureConcrete evaluate(ExpressionList values, VariableNameList iface) throws Exception {
		
		Definitions definitions = new Definitions();		

		// Try to find the parameter value for a correct number of nodes
		int k_params = 0;
		VariableRange rng_params = null;
		for (Parameter param : params) {
			if (param.getVariable() instanceof VariableName) {
				k_params += 1;
			} else if (param.getVariable() instanceof VariableRange) {
				rng_params = (VariableRange)param.getVariable();
			} else {
				throw new Exception("Parameter " + param.getVariable() + " is not a valid parameter name.");
			}
		}
		int size_params = values.size() - k_params;
		
		if (rng_params != null) {
			Definitions defs = rng_params.findParamFromSize(size_params);
			if (defs != null) {
				definitions.putAll(defs);
			} else {
				System.out.println("[error] Parameters in " + rng_params + " cannot be deduced from its length.");				
			}
		} else {
			if (size_params != 0)
				System.out.println("[error] Wrong number of parameter values: " + values);
		}

		// Find the assignment of parameters.
		Iterator<Parameter> param = params.evaluate(definitions).iterator();
		Iterator<Expression> value = values.iterator();	
		
		while (param.hasNext() && value.hasNext()) {
			Parameter x = param.next();
			Expression v = value.next();
			
			if (!(x.getVariable() instanceof VariableName)) 
				throw new Exception(x + " is not a valid parameter name.");
			
			definitions.put((VariableName)x.getVariable(), v);
		}
		
		// Find the links of the interface. 
		Map<Port, Port> links = new HashMap<Port, Port>();	
		
		Iterator<Node> node = nodes.evaluate(definitions).iterator();
		
		if (iface == null) {

			// Create a the default set of links for this interface
			while (node.hasNext()) {
				Node x = node.next();
				
				Port p = x.toPort();
				
				if (p == null)
					throw new Exception(x + " is not a valid node name.");
				
				links.put(p, p);
			}
			
		} else {

			// Try to find the parameter value for a correct number of nodes
			int k_nodes = 0;
			VariableRange rng_nodes = null;
			for (Node x : nodes) {
				if (x.getVariable() instanceof VariableName) {
					k_nodes += 1;
				} else if (x.getVariable() instanceof VariableRange) {
					rng_nodes = (VariableRange)x.getVariable();
				} else {
					throw new Exception("Parameter " + x.getVariable() + " is not a valid parameter name.");
				}
			}
			int size_nodes = iface.getList().size() - k_nodes;
			
			if (rng_nodes != null) {
				Definitions defs = rng_nodes.findParamFromSize(size_nodes);
				if (defs != null) {
					definitions.putAll(defs);
				} else {
					System.out.println("[error] Parameters in " + rng_nodes + " cannot be deduced from its length.");				
				}
			} else {
				if (size_nodes != 0)
					System.out.println("[error] Wrong number of parameter values: " + iface);
			}

			Iterator<VariableName> var = iface.getList().iterator();
			
			while (node.hasNext() && var.hasNext()) {
				Node x = node.next();
				VariableName v = var.next();
				
				Port p = x.toPort();
				Port q = x.rename(v).toPort();
				
				if (p == null)
					throw new Exception(x + " is not a valid node name.");
				
				links.put(p, q);
			}
		}
		
		return new SignatureConcrete(definitions, links);
	}

	@Override
	public boolean equalType(ParameterType t) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String toString() {
		return "" + params + nodes;
	}

}
