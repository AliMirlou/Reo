package nl.cwi.reo.interpret.blocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stringtemplate.v4.ST;

import nl.cwi.reo.interpret.semantics.Definitions;
import nl.cwi.reo.interpret.variables.Variable;
import nl.cwi.reo.semantics.Port;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.semantics.connectors.Connector;
import nl.cwi.reo.semantics.connectors.SubComponent;
import nl.cwi.reo.semantics.expressions.Expression;

/**
 * <p>A list statements that evaluated into a set of definitions and 
 * a connector. 
 * 
 * <p>A body is an immutable object.
 * @param <T> type of semantics objects
 */
public final class Body<T extends Semantics<T>> implements BlockExpression<T> {
	
	/**
	 * Definitions.
	 */
	private final Definitions<T> definitions;
	
	/**
	 * Instances.
	 */
	private final Connector<T> connector;

	/**
	 * Constructs an empty body of components and definitions.
	 */
	public Body() {
		this.definitions = new Definitions<T>();
		this.connector = new Connector<T>();
	}
	
	/**
	 * Constructs a body consisting of a set of definitions and an ordered 
	 * list of subcomponents that are composed via a given operator.
	 * @param definitions		set of definitions
	 * @param subcomponents		ordered list of subcomponents
	 * @param operator			name of composition operator
	 */
	public Body(Definitions<T> definitions, List<SubComponent<T>> subcomponents, String operator) {
		if (definitions == null || subcomponents == null)
			throw new NullPointerException();
		this.definitions = new Definitions<T>(definitions);
		this.connector = new Connector<T>(operator, subcomponents);
	}
	
	/**
	 * Constructs a body consisting of a set of definitions and a connector.
	 * @param definitions	set of definitions
	 * @param connector		connector
	 */
	public Body(Definitions<T> definitions, Connector<T> connector) {
		if (definitions == null || connector == null)
			throw new NullPointerException();
		this.definitions = new Definitions<T>(definitions);
		this.connector = connector;
	}
	
	/**
	 * Gets the set of definitions in this body.
	 * @return set of definitions in this body
	 */
	public Map<String, Expression> getDefinitions() {
		return Collections.unmodifiableMap(definitions);
	}

	/**
	 * Gets the set of unifications in this body, i.e., definitions 
	 * that unify two variables.
	 * @return set of unifications in this body
	 */
	public Map<String, Expression> getUnifications() {
		return Collections.unmodifiableMap(definitions.getUnifications());
	}
	
	/**
	 * Gets the connector of this body.
	 * @return connector of this body
	 */
	public Connector<T> getConnector() {
		return connector;
	}
	
	/**
	 * Composes a set of bodies into a single body.
	 * @param progs		set of component instances
	 */
	public static <T extends Semantics<T>> Body<T> compose(String operator, List<Body<T>> bodies) {
		Definitions<T> defs = new Definitions<T>();
		List<Connector<T>> comps = new ArrayList<Connector<T>>();
		for (Body<T> body : bodies) {
			defs.putAll(body.definitions);
			comps.add(body.getConnector());
		}
		return new Body<T>(defs, Connector.compose(operator, comps));
	}
	
	/**
	 * Instantiates this body by dropping all definitions (except 
	 * necessary unifications), joining unified ports, hiding 
	 * internal ports, and renaming external ports.
	 * @param iface		map assigning a new port to every external port 
	 * (i.e., this map implicitly defines all internal ports)
	 * @return an instantiated body.
	 */
	public Body<T> instantiate(Map<Port, Port> iface) {
		Definitions<T> defs = new Definitions<T>();
		Map<Port, Port> _iface = new HashMap<Port, Port>(iface);		
		
		// Collect all necessary unifications, and rename the variables in these definitions.
		for (Map.Entry<String, Expression> defn : this.definitions.entrySet()) {
			if (defn.getValue() instanceof Variable) {
				String a = defn.getKey();
				String b = ((Variable)defn.getValue()).getName();
				
				Port a_new = iface.get(new Port(a));
				Port b_new = iface.get(new Port(b));
				
				if (a_new != null) {
					if (b_new != null) {
						String x = a_new.getName();
						Variable y = new Variable(b_new.getName(), null);
						defs.put(x, y);
					} else {
						_iface.put(new Port(b), a_new);
					}
				} else {
					if (b_new != null) {
						_iface.put(new Port(a), b_new);
					} else {
						Port ahidden = new Port(a);
						ahidden.hide();
						_iface.put(new Port(b), new Port(a).hide());
					}
				}
			}
		}
		
		return new Body<T>(defs, connector.reconnect(_iface));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Body<T> evaluate(Map<String, Expression> params) {
		Definitions<T> definitions_p = definitions.evaluate(params);
		// TODO Possibly local variables in this definition get instantiated by variables from the context.
		// TODO Add code to evaluate semantics too.
		return new Body<T>(definitions_p, connector);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		ST st = new ST("{\n  <definitions><if(connector)>\n  <connector><endif>\n}");
		st.add("definitions", definitions);
		st.add("connector", connector);
		return st.render();
	}
}
