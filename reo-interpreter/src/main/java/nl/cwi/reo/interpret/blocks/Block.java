package nl.cwi.reo.interpret.blocks;

import java.util.Map;

import org.stringtemplate.v4.ST;

import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.semantics.expressions.Expression;
import nl.cwi.reo.semantics.expressions.StringExpression;

public final class Block<T extends Semantics<T>> implements BlockExpression<T> {
	
	private final StringExpression operator;
	
	private final StatementList<T> statements; 

	public Block(StringExpression operator, StatementList<T> statements) {
		this.operator = operator;
		this.statements = statements;
	}

	@Override
	public Block<T> evaluate(Map<String, Expression> params) {
		return new Block<T>(operator.evaluate(params), statements.evaluate(params));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		ST st = new ST("<operator><statements>");
		st.add("operator", operator);
		st.add("statements", statements);
		return st.render();
	}

}
