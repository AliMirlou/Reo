package nl.cwi.reo.interpret.blocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.stringtemplate.v4.ST;

import nl.cwi.reo.interpret.semantics.Definitions;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.semantics.expressions.Expression;
import nl.cwi.reo.semantics.expressions.GroundExpression;

public final class StatementList<T extends Semantics<T>> implements Statement<T> {
	
	/**
	 * List of statements.
	 */
	private final List<Statement<T>> statements;

	/**
	 * Constructs a block of statements.
	 * @param statements		list of statements
	 */
	public StatementList(List<Statement<T>> statements) {
		if (statements == null)
			throw new NullPointerException();
		List<Statement<T>> stmts = new ArrayList<Statement<T>>();
		for (Statement<T> stmt : statements) {
			if (stmt == null)
				throw new NullPointerException();
			stmts.add(stmt);
		}
		this.statements = Collections.unmodifiableList(stmts);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public StatementList<T> evaluate(Map<String, Expression> params) {	
		
//		System.out.println("[info] Evaluating " + this+ " using " + params.keySet());

		Definitions<T> definitions = new Definitions<T>(params);
		
		List<Statement<T>> stmts = new ArrayList<Statement<T>>();
		List<Statement<T>> stmts_p = new ArrayList<Statement<T>>(this.statements);	
		List<Body<T>> progs = new ArrayList<Body<T>>(); 
		boolean loop = true;
		
		while (loop) {
			
			stmts = new ArrayList<Statement<T>>(stmts_p);
			stmts_p = new ArrayList<Statement<T>>();			
			progs = new ArrayList<Body<T>>();
			loop = false;
			
			for (Statement<T> s : stmts) {			
				
				Statement<T> s_p = s.evaluate(definitions);
				
				if (s_p instanceof StatementList<?>) {
					stmts_p.addAll(((StatementList<T>)s_p).statements);
					loop = true;
				} else if (s_p instanceof Body) {
					stmts_p.add(s_p);
					progs.add((Body<T>)s_p);
					Map<String, Expression> progDefs = ((Body<T>)s_p).getDefinitions();
					for (Map.Entry<String, Expression> def : progDefs.entrySet()) {
						if (!definitions.containsKey(def.getKey())) {
							definitions.put(def.getKey(), def.getValue());
							if (def.getValue() instanceof GroundExpression) 
								loop = true;
						} else {
//							if (!def.getValue().equals(definitions.get(def.getKey())))
//								throw new CompilationException(null, "Violated assertion.");
							// TODO If redefined evaluates to false: throw an error.
						}
					}
				} else {
					stmts_p.add(s_p);
				}
			}
			
		}
//		System.out.println("[info] block\n" + this + "\nevaluates to block");
//		System.out.println(prog + "\nusing " + params.keySet());
		return new StatementList<T>(stmts_p);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		ST st = new ST("{\n  <statements; separator=\"\n\">\n}");
		st.add("statements", statements);
		return st.render();
	}
}
