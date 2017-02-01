package nl.cwi.reo.interpret.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.stringtemplate.v4.ST;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.semantics.Definitions;
import nl.cwi.reo.interpret.variables.Variable;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.semantics.expressions.Expression;
import nl.cwi.reo.semantics.expressions.IntegerExpression;
import nl.cwi.reo.semantics.expressions.IntegerValue;

/**
 * A parameterized for loop of a set {link java.util.Set}&lt;{link nl.cwi.reo.parse.Component}&gt; of parameterized components.
 */
public class ForLoop<T extends Semantics<T>> implements Statement<T> {

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
	public Statement<T> statement;

	/**
	 * Constructs a parameterized for loop. 
	 * 
	 * @param parameter		name of the iteration parameter
	 * @param lower			expression defining the lower iteration bound
	 * @param upper			expression defining the upper iteration bound
	 * @param subprogram	iterated subprogram definition
	 */
	public ForLoop(Variable parameter, IntegerExpression lower, IntegerExpression upper, Statement<T> statement) {
		if (parameter == null || lower == null || upper == null || statement == null)
			throw new NullPointerException();
		this.parameter = parameter;
		this.lower = lower;
		this.upper = upper;
		this.statement = statement;
	}
	
	/**
	 * Gets a {link nl.cwi.reo.ProgramInstance} for a particular parameter assignment.
	 * @param parameters		parameter assignment
	 * @return Program instance {link nl.cwi.reo.ProgramInstance} for this parameterized component
	 */
	@Override
	public Statement<T> evaluate(Map<String, Expression> params) {
		
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
			List<Statement<T>> statements = new ArrayList<Statement<T>>();
			List<Body<T>> bodies = new ArrayList<Body<T>>();
			for (int i = a; i <= b; i++) {
				defns.put(parameter.getName(), new IntegerValue(Integer.valueOf(i)));
				Statement<T> e = statement.evaluate(defns);
				statements.add(e);
				if (e instanceof Body) {
					bodies.add((Body<T>)e);
				} else {
					isProgram = false;
				}
			}
			
			if (isProgram) 
				return Body.compose("", bodies).remove(parameter.getName());
			
			return new StatementList<T>(statements);
		}
		
		return new ForLoop<T>(parameter, x, y, statement.evaluate(params));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		ST st = new ST("for <parameter>=<lower>..<upper> <block>");
		st.add("parameter", parameter);
		st.add("lower", lower);
		st.add("upper", upper);
		st.add("block", statement);
		return st.render();
	}
}
