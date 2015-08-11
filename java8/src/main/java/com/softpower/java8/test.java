package com.softpower.java8;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class test {

	@AllArgsConstructor
	@ToString
	static class User {
		@Getter @Setter private String name;
		@Getter @Setter private Integer age;

		public User(String name) {
			this.name = name;
			this.age = -1;
		}

		public String getAgeMetric(int age) {
			if (age > 15) {
				return "old";
			}else {
				return "young";
			}
		}
	}

	public static void display(Supplier<String> supplier) {
		System.out.println(supplier.get());
	}

	public class Document {
	     public String getPageContent(int pageNumber) {
	         return Integer.toString(pageNumber);
	     }
	}

	public static void main(String[] args) {
		List<User> users = Arrays.asList(new User("user1", 10), new User("user2", 20), new User("user3", 16), new User("user4", 8));

		Map<String, User> map = users.stream().collect(Collectors.toMap(t -> t.getName(), t -> t));
		System.out.println(map);

		List<String> usernames = users.stream().map(User::getAge).map(age -> Integer.toString(age)).collect(toList());
		System.out.println(usernames);

		long userCount = users.stream().mapToInt(User::getAge).filter(age -> age > 10).count();
		System.out.println(userCount);
		display(() -> UUID.randomUUID().toString());

		User user = new User("1", 1);
		users.stream().map(User::getAge).map(user::getAgeMetric).forEach(System.out::print);
		Arrays.stream(new int[] {1, 2, 3, 4, 5}).mapToObj(user::getAgeMetric).forEach(System.out::print);

		List<User> usersAgain = usernames.stream().map(User::new).collect(toList());
		System.out.println(usersAgain);
	}

}
