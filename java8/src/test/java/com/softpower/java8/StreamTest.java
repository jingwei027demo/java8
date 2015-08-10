package com.softpower.java8;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamTest {
	static final int DEFAULT_SAMPLE_SIZE = 50000000;

	List<String> getSampleData(int size) {
		List<String> cols = new ArrayList<>();
		String [] words = {"dog", "cat", "robot", "blue", "fruit", "apple", "fire", "light", "boy"};
		for (int i=0; i<size; i++) {
			cols.add(words[i % words.length]);
		}
		return cols;
	}

	List<Integer> getSampleIntData(int size) {
		List<Integer> cols = new ArrayList<>();
		for (int i=0; i<size; i++) {
			cols.add(i);
		}
		return cols;
	}

	@Test
	public void testStreamToList() {
		List<Integer> cols = Stream.of(1, 2, 3, 4, 5).collect(toList());
		System.out.println(cols);
	}

	@Test
	public void testStreamForEach() {
		Stream<Integer> s = Stream.of(1, 2, 3, 4, 5);
		s.forEach(each -> System.out.println(each));


	}

	private long methodLegacy(List<String> cols) {
		long count = 0;
		for (String per : cols) {
			if (!"robot".equals(per) && !"fruit".equals(per)) {
				count ++;
			}
		}
		return count;
	}

	private long methodLambda(List<String> cols) {
		return cols.stream().filter(s -> {
			return !"robot".equals(s);
		}).filter(s -> {
			return !"fruit".equals(s);
		}).count();
	}

	private long methodLambdaParallel(List<String> cols) {
		return cols.stream().parallel().filter(s -> {
			return !"robot".equals(s);
		}).filter(s -> {
			return !"fruit".equals(s);
		}).count();
	}

	@Test
	public void testStreamLegacy() {
		List<String> cols = getSampleData(DEFAULT_SAMPLE_SIZE);
		final long beginTime = System.currentTimeMillis();
		long count = methodLegacy(cols);
		System.out.println(count);
		System.out.println(System.currentTimeMillis() - beginTime); // 408
	}

	@Test
	public void testStreamLambda() {
		List<String> cols = getSampleData(DEFAULT_SAMPLE_SIZE);
		final long beginTime = System.currentTimeMillis();
		long count = methodLambda(cols);
		System.out.println(count);
		System.out.println(System.currentTimeMillis() - beginTime); // 1012
	}

	@Test
	public void testStreamLambdaParallel() {
		List<String> cols = getSampleData(DEFAULT_SAMPLE_SIZE);
		final long beginTime = System.currentTimeMillis();
		long count = methodLambdaParallel(cols);
		System.out.println(count);
		System.out.println(System.currentTimeMillis() - beginTime); // 394
	}

	private List<String> methodLegacyMap(List<String> cols) {
		List<String> newCols = new ArrayList<>();
		String tmpStr = null;
		for (String str : cols) {
			tmpStr = str.toUpperCase();
			tmpStr = "[" + tmpStr + "]";
			newCols.add(tmpStr);
		}
		return newCols;
	}

	private Function<String, String> toUpperCase() {
		return t -> t.toUpperCase();
	}

	private Function<String, String> wrapSomething() {
		return t -> "[" + t.toUpperCase() + "]";
	}

	private List<String> methodLambdaMap(List<String> cols, boolean parallel) {
		List<String> newCols = new ArrayList<>();
		if (parallel) {
			newCols = cols.stream().parallel()
					.map(toUpperCase())
					.map(wrapSomething())
					.collect(Collectors.toList());
		}else {
			newCols = cols.stream()
					.map(toUpperCase())
					.map(wrapSomething())
					.collect(Collectors.toList());
		}
		return newCols;
	}

	@Test
	public void testMapLegacy() {
		List<String> cols = getSampleData(500000);
		final long beginTime = System.currentTimeMillis();
		cols = methodLegacyMap(cols);
		System.out.println(System.currentTimeMillis() - beginTime); // 151
	}

	@Test
	public void testMapLambda() {
		List<String> cols = getSampleData(500000);
		final long beginTime = System.currentTimeMillis();
		cols = methodLambdaMap(cols, false);
		System.out.println(System.currentTimeMillis() - beginTime); // 257
	}

	@Test
	public void testMapLambdaParallel() {
		List<String> cols = getSampleData(500000);
		final long beginTime = System.currentTimeMillis();
		cols = methodLambdaMap(cols, true);
		System.out.println(System.currentTimeMillis() - beginTime); // 254
	}

	@Test
	public void testMapNewTypeLambda() {
		List<String> cols = getSampleData(500000);
		List<Integer> lens = cols.stream().map(s -> s.length()).collect(toList());
		System.out.println(lens.get(0));
		System.out.println(lens.get(1));
		System.out.println(lens.get(2));
	}

	@Test
	public void testFlatMapLambda() {
		List<String> cols1 = getSampleData(100);
		List<String> cols2 = getSampleData(100);
		List<String> cols3 = null;
//		cols3 = Stream.of(cols1, cols2).map(cols -> cols.stream().map(s -> s.toUpperCase()));
		cols3 = Stream.of(cols1, cols2).flatMap(cols -> cols.stream()).map(s -> s.toUpperCase()).collect(toList());
		Assert.assertEquals(200, cols3.size());
	}

	@Test
	public void testMinMax() {
		List<Integer> cols = getSampleIntData(1000);
		int minInt = cols.stream().min(comparing(i -> i)).get();
		int maxInt = cols.stream().max(Comparator.comparing(i -> i)).get();
		System.out.println(minInt);
		System.out.println(maxInt);
		Assert.assertEquals(0, minInt);
		Assert.assertEquals(999, maxInt);
	}

	@Test
	public void testMinOptional() {
		List<Integer> cols = getSampleIntData(0);
		Optional<Integer> minIntOpt = cols.stream().min(comparing(i -> i));
		int minInt = minIntOpt.orElse(-1);
		System.out.println(minIntOpt.isPresent());
		System.out.println(minInt);
		Assert.assertEquals(-1, minInt);
	}

	private BinaryOperator<Integer> sumOperator() {
		return (t, u) -> t + u;
	}

	@Test
	public void testSum() {
		List<Integer> cols = getSampleIntData(10);
		int sum = cols.stream().reduce(0, (acc, p) -> acc + p);
		System.out.println(sum);

		List<Integer> cols2 = getSampleIntData(0);
		Optional<Integer> sumOpt = cols2.stream().reduce((acc, p) -> acc + p);
		System.out.println(sumOpt.isPresent());
		System.out.println(sumOpt.orElse(0));
	}

	@Test
	public void testSum2() {
		List<Integer> cols = getSampleIntData(10);
		System.out.println(cols.stream().reduce(0, sumOperator()));
		System.out.println(cols.stream().reduce(sumOperator()).orElse(0));
	}

	@Test
	public void testBoxingUnBoxing() {
		List<String> cols = getSampleData(DEFAULT_SAMPLE_SIZE);

		long beginTime = System.currentTimeMillis();
		IntSummaryStatistics intSS = cols.stream().mapToInt(per -> per.length()).summaryStatistics();
		System.out.println(System.currentTimeMillis() - beginTime); // 364
		System.out.println(intSS.getSum());

		beginTime = System.currentTimeMillis();
		int sumLens = cols.stream().mapToInt(per -> per.length()).reduce(0, (acc, per) -> acc + per);
		System.out.println(System.currentTimeMillis() - beginTime); // 290
		System.out.println(sumLens);

		beginTime = System.currentTimeMillis();
		sumLens = cols.stream().map(per -> per.length()).reduce(0, (acc, per) -> acc + per);
		System.out.println(System.currentTimeMillis() - beginTime); // 6447
		System.out.println(sumLens);
	}

}
