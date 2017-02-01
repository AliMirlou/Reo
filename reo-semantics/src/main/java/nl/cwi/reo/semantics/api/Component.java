package nl.cwi.reo.semantics.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stringtemplate.v4.ST;

//import nl.cwi.reo.interpret.strings.StringValue;

public final class Component<T extends Semantics<T>> implements SubComponent<T> {
	
	private final T semantics;
	
	private final SourceCode source;
	
	private final Map<Port, Port> links;
	
	/**
	 * Constructs a new atomic component.
	 * @param atom		semantics
	 */
	public Component(T atom) {
		this.semantics = atom;
		this.source = new SourceCode();
		Map<Port, Port> links = new HashMap<Port, Port>();
		for (Port p : semantics.getInterface())
			links.put(p, p);
		this.links = Collections.unmodifiableMap(links);
	}
	
	/**
	 * Constructs a new atomic component.
	 * @param semantics		semantics
	 * @param source	reference to source code
	 */
	public Component(T semantics, SourceCode source) {
		this.semantics = semantics;
		this.source = source;
		Map<Port, Port> links = new HashMap<Port, Port>();
		for (Port p : semantics.getInterface())
			links.put(p, p);
		this.links = Collections.unmodifiableMap(links);
	}
	
	public Component(T semantics, SourceCode source, Map<Port, Port> links) {
		this.semantics = semantics;
		this.source = source;
		this.links = Collections.unmodifiableMap(links);
	}
	
	/**
	 * Gets the semantics object of this atomic componnet.
	 * @return Semantics object
	 */
	public T getSemantics() {
		return semantics;
	}
	
	/**
	 * Gets the source code reference.
	 * @return source code reference.
	 */
	public SourceCode getSourceCode() {
		return source;
	}

	/**
	  * {@inheritDoc}
	  */
	@Override
	public Map<Port, Port> getLinks() {
		return links;
	}

	/**
	  * {@inheritDoc}
	  */
	@Override
	public Component<T> evaluate(Map<String, Expression> params) {
		return new Component<T>(semantics.evaluate(params), source);
	}

	/**
	  * {@inheritDoc}
	  */
	@Override
	public SubComponent<T> reconnect(Map<Port, Port> joins) {
		return new Component<T>(semantics, source, Links.reconnect(links, joins));
	}

	/**
	  * {@inheritDoc}
	  */
	@Override
	public SubComponent<T> renameHidden(Integer i) {
		return new Component<T>(semantics, source, Links.renameHidden(links, i));
	}

	/**
	  * {@inheritDoc}
	  */
	@Override
	public List<Component<T>> flatten() {
		List<Component<T>> list = new ArrayList<Component<T>>();
		list.add(this);
		return list;
	}

	/**
	  * {@inheritDoc}
	  */
	@Override
	public SubComponent<T> insertNodes(boolean mergers, boolean replicators, T nodeFactory) {
		return this;
	}

	/**
	  * {@inheritDoc}
	  */
	@Override
	public List<T> integrate() {
		List<T> list = new ArrayList<T>();
		list.add(semantics.rename(links));
		return list;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		ST st = new ST("{\n  <semantics>\n  <source>\n}");
		st.add("semantics", semantics);
		st.add("source", source);
		return st.render();
	}
}
