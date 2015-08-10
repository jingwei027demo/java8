package com.softpower.java8;

import org.junit.Test;


public class FunctionalRuleTest {

//	@FunctionalInterface
	public interface Overload {
		public boolean test(Object value);
	}

	public interface IntOverload {
		public boolean test(int value);
	}

	private void overloadMethod(Overload overload) {
		System.out.println("overload");
	}

	private void overloadMethod(IntOverload overload) {
		System.out.println("int-overload");
	}

	@Test
	public void testOverload() {
		overloadMethod((Object x) -> true);
		overloadMethod((int x) -> true);
//		overloadMethod(x -> true);
	}
}
