package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.cwi.reo.semantics.Port;
import nl.cwi.reo.semantics.Semantics;

public final class Program implements ProgramExpression {
	
	/**
	 * Signature expression.
	 */
	private final Signature sign;
	
	/**
	 * Definitions.
	 */
	private final Map<VariableName, Expression> defns;
	
	/**
	 * Instances.
	 */
	private final List<Instance> instances;

	/**
	 * Constructs an empty body of components and definitions.
	 */
	public Program() {
		this.sign = new Signature();
		this.defns = new HashMap<VariableName, Expression>();
		this.instances = new ArrayList<Instance>();
	}
	
	public Program(Signature sign, Semantics<?> atom) {
		if (sign == null || atom == null)
			throw new IllegalArgumentException("Arguments cannot be null.");
		this.sign = sign;
		this.defns = new HashMap<VariableName, Expression>();
		Map<String, Port> links = new HashMap<String, Port>();
		for (String a : atom.getInterface()) 
			links.put(a, new Port(a));
		List<Instance> instances = new ArrayList<Instance>();
		instances.add(new Instance(atom, links));
		this.instances = Collections.unmodifiableList(instances);
	}
	

	
	public Program(Signature sign, List<Instance> instances) {
		if (sign == null || atom == null)
			throw new IllegalArgumentException("Arguments cannot be null.");
		this.sign = sign;
		this.defns = new HashMap<VariableName, Expression>();
		this.instances = Collections.unmodifiableList(instances);
	}
		
	/**
	 * Constructs a collection of definitions and component instances.
	 * @param definitions	map of definitions
	 * @param instance		single component instance
	 */
	public Program(Map<VariableName, Expression> definitions) {
		if (definitions == null)
			throw new IllegalArgumentException("Argument cannot be null.");
		this.sign = new Signature();
		this.defns = Collections.unmodifiableMap(definitions);
		this.instances = Collections.unmodifiableList(new ArrayList<Instance>());
	}

	/**
	 * Constructs a collection of definitions and component instances.
	 * @param instance		single component instance
	 */
	public Program(Instance instance) {
		if (instance == null)
			throw new IllegalArgumentException("Argument cannot be null.");
		this.sign = new Signature();
		this.defns = Collections.unmodifiableMap(new HashMap<VariableName, Expression>());
		List<Instance> instances = new ArrayList<Instance>();
		instances.add(instance);
		this.instances = Collections.unmodifiableList(instances);
	}

	/**
	 * Constructs a collection of definitions and component instances.
	 * @param definitions	map of definitions
	 * @param instance		single component instance
	 */
	public Program(Map<VariableName, Expression> definitions, Instance instance) {
		if (definitions == null || instance == null)
			throw new IllegalArgumentException("Arguments cannot be null.");
		this.sign = new Signature();
		this.defns = Collections.unmodifiableMap(definitions);
		List<Instance> instances = new ArrayList<Instance>();
		instances.add(instance);
		this.instances = Collections.unmodifiableList(instances);
	}

	/**
	 * Constructs a collection of definitions and component instances.
	 * @param definitions	map of definitions
	 * @param instances		list of component instances
	 */
	public Program(Map<VariableName, Expression> definitions, List<Instance> instances) {
		if (definitions == null || instances == null)
			throw new IllegalArgumentException("Arguments cannot be null.");
		this.sign = new Signature();
		this.defns = Collections.unmodifiableMap(definitions);
		this.instances = Collections.unmodifiableList(instances);
	}
	
	public Expression get(VariableName x) {
		return defns.get(x);
	}
	
	public void put(VariableName x, Expression e) {
		defns.put(x, e);
	}
	
	public void putAll(Map<VariableName, Expression> defs) {
		defns.putAll(defs);
	}
	
	public void remove(VariableName x) {
		defns.remove(x);
	}
	
	/**
	 * Gets the component instances.
	 * @return set of component instances.
	 */
	public Map<VariableName, Expression> getDefinitions() {
		return defns;
	}
	
	/**
	 * Gets the component instances.
	 * @return set of component instances.
	 */
	public List<Instance> getInstances() {
		return instances;
	}
	
	/**
	 * Composes a set of programs into a single program.
	 * @param progs		set of component instances
	 */
	public Program compose(Program body) {
		Map<VariableName, Expression> _defns = 
				new HashMap<VariableName, Expression>(defns);
		_defns.putAll(body.defns); // TODO IS THIS OK? e.g., in case of recursion, this code just overwrites the old definition.
		List<Instance> _instances = new ArrayList<Instance>();
		Integer i = 0;
		for (Instance comp : this.instances) 
			_instances.add(comp.renameHidden(i));
		for (Instance comp : body.instances)
			_instances.add(comp.renameHidden(i));
		return new Program(_defns, _instances);
	}
	
	/**
	 * Composes a set of programs into a single program.
	 * @param progs		set of component instances
	 */
	public Program compose(List<Program> bodies) {
		Map<VariableName, Expression> _defns = 
				new HashMap<VariableName, Expression>(defns);
		List<Instance> _instances = new ArrayList<Instance>();
		Integer i = 0;
		for (Instance comp : this.instances) 
			_instances.add(comp.renameHidden(i));
		for (Program body : bodies) {
			_defns.putAll(body.defns);
			for (Instance comp : body.instances)
				_instances.add(comp.renameHidden(i));
		}
		return new Program(_defns, _instances);
	}
	
	/**
	 * Relabels the interface of this component.
	 * @param iface		maps old node names to new node names.
	 */
	public InstanceList restrictAndRename(Map<Port, Port> iface) {	
		List<Instance> newinstances = new ArrayList<Instance>();
		for (Instance comp : instances)			
			newinstances.add(comp.restrictAndRename(iface));
		return new InstanceList(newinstances);
	}

	@Override
	public Program evaluate(Map<VariableName, Expression> params) throws Exception {
		Map<VariableName, Expression> defns_p = new HashMap<VariableName, Expression>();
		for (Map.Entry<VariableName, Expression> def : defns.entrySet()) 
			defns_p.put(def.getKey(), def.getValue().evaluate(params));
		return new Program(defns_p, instances);
		// TODO Add code to evaluate semantics too.
		// TODO Possibly local variables in this definition get instantiated by variables from the context.
	}

	@Override
	public Component instantiate(ExpressionList values, Interface iface) throws Exception {
		SignatureInstance v = sign.evaluate(values, iface);
		List<Instance> _instances = inst.evaluate(v.getDefinitions()).restrictAndRename(v.getLinks());
		for (Instance comp : instances) {
			_instances.add(comp.e)
		}
		return new Program(sign, _instances);	
	}
	
	@Override
	public String toString() {
		// TODO use ANTLR string template to generate pretty strings.
		return sign + "{" + defns + "\n" + instances + "}";
	}
}
