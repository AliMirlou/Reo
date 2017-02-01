package nl.cwi.reo.interpret.blocks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nl.cwi.reo.interpret.components.ComponentAtom;
import nl.cwi.reo.interpret.semantics.Definitions;
import nl.cwi.reo.interpret.strings.StringValue;
import nl.cwi.reo.semantics.api.BooleanValue;
import nl.cwi.reo.semantics.api.Expression;
import nl.cwi.reo.semantics.api.IntegerValue;
import nl.cwi.reo.semantics.api.Semantics;

public class BlockList<T extends Semantics<T>> implements Block<T> {
	
	/**
	 * List of blocks.
	 */
	public List<Block<T>> blocks;

	/**
	 * Constructs a list of blocks.
	 * @param blocks		list of blocks
	 */
	public BlockList(List<Block<T>> blocks) {
		if (blocks == null)
			throw new NullPointerException();
		for (Block<T> stmt : blocks)
			if (stmt == null)
				throw new NullPointerException();
		this.blocks = blocks;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Block<T> evaluate(Map<String, Expression> params) {	
		
//		System.out.println("[info] Evaluating " + this+ " using " + params.keySet());

		Definitions<T> definitions = new Definitions<T>(params);
		
		List<Block<T>> stmts = new ArrayList<Block<T>>();
		List<Block<T>> stmts_p = new ArrayList<Block<T>>(this.blocks);	
		List<Body<T>> progs = new ArrayList<Body<T>>(); 
		boolean isProgramValue = true;
		boolean loop = true;
		
		while (loop) {
			
			stmts = new ArrayList<Block<T>>(stmts_p);
			stmts_p = new ArrayList<Block<T>>();			
			progs = new ArrayList<Body<T>>();
			isProgramValue = true;
			loop = false;
			
			for (Block<T> s : stmts) {			
				
				Block<T> s_p = s.evaluate(definitions);
				stmts_p.add(s_p);
				
				if (s_p instanceof Body) {
					progs.add((Body<T>)s_p);
					Map<String, Expression> progDefs = ((Body<T>)s_p).getDefinitions();
					for (Map.Entry<String, Expression> def : progDefs.entrySet()) {
						if (!definitions.containsKey(def.getKey())) {
							definitions.put(def.getKey(), def.getValue());
							if (def.getValue() instanceof BooleanValue) loop = true;
							if (def.getValue() instanceof IntegerValue) loop = true;
							if (def.getValue() instanceof StringValue) loop = true;
							if (def.getValue() instanceof ComponentAtom) loop = true;
						} else {
//							if (def.getValue().equals(definitions.get(def.getKey())))
//								throw new CompilationException(null, null);
							// TODO If redefined evaluates to false: throw an error.
						}
					}
				} else {
					isProgramValue = false;
				}
			}
			
		}
		
		Block<T> prog = null;
		
		if (isProgramValue) {
			prog = Body.compose("", progs);
		} else {
			prog = new BlockList<T>(stmts_p);
		}
		
		System.out.println("[info] " + this + " evaluates to ");
		System.out.println("       " + prog + " using " + params.keySet());
		return prog;
	}
	
	@Override
	public String toString() {
		String s = "{";
		Iterator<Block<T>> stmt = blocks.iterator();
		while (stmt.hasNext()) 
			s += stmt.next() + (stmt.hasNext() ? ", " : "");
		return s + "}";
	}
}
