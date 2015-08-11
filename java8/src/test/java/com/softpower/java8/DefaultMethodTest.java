package com.softpower.java8;

import org.junit.Test;


public class DefaultMethodTest {

	interface Father {
		public default void welcome() {
			System.out.println("Father");
		}
	}

	interface Mother {
		public default void welcome() {
			System.out.println("Mother");
		}
	}

	interface Child extends Father {
		@Override
		public default void welcome() {
			System.out.println("Child");
		}
	}

	class FatherImpl implements Father {

	}

	class ChildImpl implements Child {

	}

	class Father2Impl extends FatherImpl {
		@Override
		public void welcome() {
			System.out.println("Father2Impl");
		}
	}

	class Father3Impl extends Father2Impl implements Child {

	}

	class FatherMotherImpl implements Father, Mother {
		String body = null;

		@Override
		public void welcome() {
			Mother.super.welcome();
		}
	}

	@Test
	public void testDefaultMethod() {
		/**
		 * FatherImpl implements Father
		 */
		Father f = new FatherImpl();
		f.welcome();	// Father

		/**
		 * ChildImpl implements Child
		 */
		Child c = new ChildImpl();
		c.welcome();	// Child

		/**
		 * Father2Impl extends FatherImpl
		 */
		Father f2 = new Father2Impl();
		f2.welcome();	// Father2Impl

		/**
		 * Father3Impl extends Father2Impl implements Child
		 */
		Father f3 = new Father3Impl();
		f3.welcome();	// Father2Impl (*) concreate override method win default method

		/**
		 * FatherMotherImpl implements Father, Mother
		 */
		FatherMotherImpl fm = new FatherMotherImpl();
		fm.welcome();	// Mother
	}

}
