package nl.cwi.reo.semantics.expressions;

import java.util.Map;

/**
 * A ValueExpression is an expression that cannot evaluate to a list of expressions, 
 * such as integer expressions, boolean expressions, string expressions, component
 * definitions. 
 * On the contrary, a variable range a[1..3] does evaluate to a list: &lt;a[1],a[2],a[3]&gt;.
 */
public interface AtomicExpression extends Expression {
	
	public AtomicExpression evaluate(Map<String, Expression> params);

}
