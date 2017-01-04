package nl.cwi.reo.interpret;

import java.util.Map;


public final class IntegerValue implements IntegerExpression {
	
	/**
	 * Natural number.
	 */
	private final int n;
	
	/**
	 * Constructs a natural number from a string.
	 * @param s 	string representation of a natural number
	 */
	public IntegerValue(int n) {
		this.n = n;
	}
	
	public int toInteger() {
		return this.n;
	}
	
	@Override
	public String toString() {
		return "" + n;
	}

	/**
	 * Evaluates this natural number to a Integer.
	 * @param params		 	parameter assignment
	 * @return Integer evaluation with respect to parameter assignment.
	 */
	public IntegerExpression evaluate(Map<VariableName, Expression> params) throws Exception {
		return new IntegerValue(this.n);
	}
	
	public static IntegerValue addition(IntegerValue v1, IntegerValue v2) {
		return new IntegerValue(v1.n + v2.n);
	}
	
	public static IntegerValue substraction(IntegerValue v1, IntegerValue v2) {
		return new IntegerValue(v1.n - v2.n);
	}
	
	public static IntegerValue multiplication(IntegerValue v1, IntegerValue v2) {
		return new IntegerValue(v1.n * v2.n);
	}
	
	public static IntegerValue division(IntegerValue v1, IntegerValue v2) throws Exception {
		if (v2.n == 0) 
			throw new Exception("Cannot divide by zero.");
		return new IntegerValue(v1.n / v2.n);
	}
	
	public static IntegerValue remainder(IntegerValue v1, IntegerValue v2) throws Exception {
		if (v2.n == 0) 
			throw new Exception("Modulus cannot be zero.");
		return new IntegerValue(v1.n % v2.n);
	}
	
	public static IntegerValue exponentiation(IntegerValue v1, IntegerValue v2) {
		return new IntegerValue((int)Math.pow(v1.n, v2.n));
	}
	
	public static IntegerValue unarymin(IntegerValue v) {
		return new IntegerValue(-v.n);
	}

	public static BooleanValue leq(IntegerValue v1, IntegerValue v2) {
		return new BooleanValue(v1.n <= v2.n);
	}

	public static BooleanValue lt(IntegerValue v1, IntegerValue v2) {
		return new BooleanValue(v1.n < v2.n);
	}

	public static BooleanValue geq(IntegerValue v1, IntegerValue v2) {
		return new BooleanValue(v1.n >= v2.n);
	}

	public static BooleanValue gt(IntegerValue v1, IntegerValue v2) {
		return new BooleanValue(v1.n > v2.n);
	}

	public static BooleanValue eq(IntegerValue v1, IntegerValue v2) {
		return new BooleanValue(v1.n == v2.n);
	}

	public static BooleanValue neq(IntegerValue v1, IntegerValue v2) {
		return new BooleanValue(v1.n != v2.n);
	}	
}
