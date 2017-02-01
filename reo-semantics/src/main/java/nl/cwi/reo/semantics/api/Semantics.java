package nl.cwi.reo.semantics.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Semantics<T> {
	
	public Set<Port> getInterface();
	
	public SemanticsType getType();
		
	public T getNode(Set<Port> node);
	
	public T rename(Map<Port, Port> links);

	public T evaluate(Map<String, Expression> params);
	
	public T compose(List<T> automata);
	
	public T restrict(Collection<? extends Port> intface);
	
}
