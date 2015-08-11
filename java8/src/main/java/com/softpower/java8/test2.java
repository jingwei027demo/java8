package com.softpower.java8;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class test2 {

	public static void main(String[] args) {
		new Thread(() -> System.out.println(333331)).start();

		List<String> cols = Arrays.asList("11", "2", "333", "4444");
		cols = cols.stream().filter(s -> !"2".equals(s)).collect(Collectors.toList());
		System.out.println(cols);

		List<Integer> intCols = cols.stream().map(s -> s.length()).collect(Collectors.toList());
		System.out.println(intCols);
	}

}
