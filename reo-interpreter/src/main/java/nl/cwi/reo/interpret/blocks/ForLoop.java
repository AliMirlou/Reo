package nl.cwi.reo.interpret.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.semantics.Definitions;
import nl.cwi.reo.interpret.variables.Variable;
import nl.cwi.reo.semantics.api.Expression;
import nl.cwi.reo.semantics.api.IntegerExpression;
import nl.cwi.reo.semantics.api.IntegerValue;
import nl.cwi.reo.semantics.api.Semantics;

/**
 * A parameterized for loop of a set {link java.util.Set}&lt;{link nl.cwi.reo.parse.Component}&gt; of parameterized components.
 */
public class ForLoop<T extends Semantics<T>> implements Block<T> {

	/**
	 * Name of the iterated parameter.
	 */
	public Variable parameter;

	/**
	 * Lower bound of iteration.
	 */
	public IntegerExpression lower;

	/**
	 * Upper bound of iteration.
	 */
	public IntegerExpression upper;
	/**
	 * Iterated subprogram definition.
	 */
	public Block<T> block;

	/**
	 * Constructs a parameterized for loop. 
	 * 
	 * @param parameter		name of the iteration parameter
	 * @param lower			expression defining the lower iteration bound
	 * @param upper			expression defining the upper iteration bound
	 * @param subprogram	iterated subprogram definition
	 */
	public ForLoop(Variable parameter, IntegerExpression lower, IntegerExpression upper, Block<T> body) {
		if (parameter == null || lower == null || upper == null || body == null)
			throw new NullPointerException();
		this.parameter = parameter;
		this.lower = lower;
		this.upper = upper;
		this.block = body;
	}
	
	/**
	 * Gets a {link nl.cwi.reo.ProgramInstance} for a particular parameter assignment.
	 * @param parameters		parameter assignment
	 * @return Program instance {link nl.cwi.reo.ProgramInstance} for this parameterized component
	 * @throws Exception if the provided parameters do not match the signature of this program.
	 */
	@Override
	public Block<T> evaluate(Map<String, Expression> params) throws CompilationException {
		
		if (params.get(parameter.getName()) != null)
			throw new CompilationException(parameter.getToken(), "Parameter " + parameter + " is already used.");
		
		IntegerExpression x = lower.evaluate(params);
		IntegerExpression y = upper.evaluate(params);
		
		if (x instanceof IntegerValue && y instanceof IntegerValue) {
			
			// Evaluate the lower and upper iteration bound.
			int a = ((IntegerValue)x).toInteger();
			int b = ((IntegerValue)y).toInteger();
				
			// Iterate to find all concrete components. 
			boolean isProgram = true;
			Definitions<T> defns = new Definitions<T>(params);
			List<Block<T>> bodies = new ArrayList<Block<T>>();
			List<Body<T>> progs = new ArrayList<Body<T>>();
			for (int i = a; i <= b; i++) {
				defns.put(parameter.getName(), new IntegerValue(Integer.valueOf(i)));
				Block<T> e = block.evaluate(defns);
				bodies.add(e);
				if (e instanceof Body) {
					progs.add((Body<T>)e);
				} else {
					isProgram = false;
				}
			}
			
			if (isProgram) 
				return Body.compose("", progs).remove(parameter.getName());
			
			return new BlockList<T>(bodies);
		}
		
		return new ForLoop<T>(parameter, x, y, block.evaluate(params));
	}
	
	@Override
	public String toString() {
		return "for " + parameter + "=" + lower + ".." + upper + block;
	}
}
