package nl.cwi.reo.interpret.integers;

import java.util.Map;

import org.antlr.v4.runtime.Token;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.semantics.expressions.Expression;
import nl.cwi.reo.semantics.expressions.IntegerExpression;
import nl.cwi.reo.semantics.expressions.IntegerValue;

public final class IntegerRemainder implements IntegerExpression {

	private final IntegerExpression e1;
	
	private final IntegerExpression e2;
	
	private final Token operator;
	
	public IntegerRemainder(IntegerExpression e1, IntegerExpression e2, Token operator) {
		if (e1 == null || e2 == null)
			throw new NullPointerException();
		this.e1 = e1;
		this.e2 = e2;
		this.operator = operator;
	}

	@Override
	public IntegerExpression evaluate(Map<String, Expression> params) {
		IntegerExpression x1 = e1.evaluate(params);
		IntegerExpression x2 = e2.evaluate(params);
		try	{
			if (x1 instanceof IntegerValue && x2 instanceof IntegerValue)
				return IntegerValue.remainder((IntegerValue)x1, (IntegerValue)x2);
		} catch (Exception e) {
			throw new CompilationException(operator, e.getMessage());
		}
		return new IntegerRemainder(x1, x2, operator);
	}
	
	@Override
	public String toString() {
		return "(" + e1 + "%" + e2 + ")";
	}
}
