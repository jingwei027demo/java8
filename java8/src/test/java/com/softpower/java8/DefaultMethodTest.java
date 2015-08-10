package com.softpower.java8;

import org.junit.Test;


public class DefaultMethodTest {

	interface Father {
		public void message(String body);

		public default void welcome() {
			message("Father: Hi!");
		}

		public String getLastMessage();
	}

	interface Mother {
		public default void welcome() {
			System.out.println("Mother");
		}
	}

	interface Child extends Father {
		@Override
		public default void welcome() {
			message("Child: Hi!");
		}
	}

	class FatherImpl implements Father {
		String body = null;

		@Override
		public void message(String body) {
			this.body = body;
		}

		@Override
		public String getLastMessage() {
			return body;
		}
	}

	class ChildImpl implements Child {
		String body = null;

		@Override
		public void message(String body) {
			this.body = body;
		}

		@Override
		public String getLastMessage() {
			return body;
		}
	}

	class Father2Impl extends FatherImpl {
		/** class first **/
		@Override
		public void welcome() {
			message("Father2: Hi!");
		}
	}

	class Father3Impl extends Father2Impl implements Child {

	}

	class FatherMotherImpl implements Father, Mother {
		String body = null;

		@Override
		public void message(String body) {
			this.body = body;
		}

		@Override
		public String getLastMessage() {
			return body;
		}

		@Override
		public void welcome() {
			Mother.super.welcome();
		}
	}

	@Test
	public void testDefaultMethod() {
		Father f = new FatherImpl();
		f.welcome();
		System.out.println(f.getLastMessage()); // Father: Hi!

		Child c = new ChildImpl();
		c.welcome();
		System.out.println(c.getLastMessage()); // Child: Hi!

		Father f2 = new Father2Impl();
		f2.welcome();
		System.out.println(f2.getLastMessage()); // Father2: Hi!

		Father f3 = new Father3Impl();
		f3.welcome();
		System.out.println(f3.getLastMessage()); // Father2(when Father2Impl override welcome) / Child: Hi!

		FatherMotherImpl fm = new FatherMotherImpl();
		fm.welcome(); // Mother
	}

}
