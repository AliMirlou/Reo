package nl.cwi.reo.interpret.booleans;

import java.util.Map;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.semantics.expressions.BooleanExpression;
import nl.cwi.reo.semantics.expressions.Expression;
import nl.cwi.reo.semantics.expressions.IntegerExpression;
import nl.cwi.reo.semantics.expressions.IntegerValue;

public final class BooleanEquality implements BooleanExpression {

	private final IntegerExpression e1;
	
	private final IntegerExpression e2;
	
	public BooleanEquality(IntegerExpression e1, IntegerExpression e2) {
		if (e1 == null || e2 == null)
			throw new NullPointerException();
		this.e1 = e1;
		this.e2 = e2;
	}

	@Override
	public BooleanExpression evaluate(Map<String, Expression> params) throws CompilationException {
		IntegerExpression x1 = e1.evaluate(params);
		IntegerExpression x2 = e2.evaluate(params);
		if (x1 instanceof IntegerValue && x2 instanceof IntegerValue)
			return IntegerValue.eq((IntegerValue)x1, (IntegerValue)x2);
		return new BooleanEquality(x1, x2);
	}
	
	@Override
	public String toString() {
		return "(" + e1 + "==" + e2 + ")";
	}
}
