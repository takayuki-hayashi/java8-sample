package hayassh.java8sample;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Test;

public class Sample {

	@Test
	public void Streamの生成_iterate() throws Exception {
		Stream<String> s = Stream.iterate("a", e -> e + e).limit(4);
		assertThat(s.collect(toList()), is(Arrays.asList("a","aa","aaaa","aaaaaaaa")));
	}
	
	@Test
	public void Streamの生成_generate() throws Exception {
		Stream<String> s = Stream.generate(() -> "a").limit(4);
		assertThat(s.collect(toList()), is(Arrays.asList("a","a","a","a")));
	}
	
	@Test
	public void Streamの生成_concat() throws Exception {
		Stream<String> s1 = Stream.of("a","b");
		Stream<String> s2 = Stream.of("c","d");
		Stream<String> concat = Stream.concat(s1, s2);
		assertThat(concat.collect(toList()), is(Arrays.asList("a","b","c","d")));
	}
	
	@Test
	public void プリミティブ型用() throws Exception {
		IntStream stream = IntStream.of(1,2,3);
		DoubleStream mapToDouble = stream.mapToDouble(i -> Double.valueOf(i));
	}
	
	@Test
	public void オブジェクトからオブジェクトへ変換_map() throws Exception {
		Stream<String> stream = Stream.of("1","2","3");
		Stream<String> stream2 = stream.map(e -> e + "A");
		assertThat(stream2.collect(toList()), is(Arrays.asList("1A","2A","3A")));
	}
	
	@Test
	public void オブジェクトからオブジェクトへ変換_flatMap() throws Exception {
		Stream<String> stream = Stream.of("1,2,3","4,5,6");
		Stream<String> stream2 = stream.flatMap(e -> Stream.of(e.split(",")));
		assertThat(stream2.collect(toList()), is(Arrays.asList("1","2","3","4","5","6")));
	}
	
	@Test
	public void 重複削除() throws Exception {
		Stream<String> distinct = Stream.of("a","b","b","c").distinct();
		assertThat(distinct.collect(toList()), is(Arrays.asList("a","b","c")));
	}
	
	@Test
	public void ソート() throws Exception {
		Stream<Integer> sorted = Stream.of(5,1,2,3,4).sorted();
		assertThat(sorted.collect(toList()), is(Arrays.asList(1,2,3,4,5)));
	}
	
	@Test
	public void ソート2() throws Exception {
		Stream<Integer> sorted = Stream.of(5,1,2,3,4).sorted((e1,e2) -> e1.compareTo(e2) * -1);
		assertThat(sorted.collect(toList()), is(Arrays.asList(5,4,3,2,1)));
	}
	
	@Test
	public void peek() throws Exception {
		Stream.of("a","b","c").peek(System.out::println).forEach(System.out::println);
	}
	
	@Test
	public void limit() throws Exception {
		Stream<String> limit = Stream.of("a","b","c").limit(2);
		assertThat(limit.collect(toList()), is(Arrays.asList("a","b")));
	}
	
	@Test
	public void skip() throws Exception {
		Stream<String> skip = Stream.of("a","b","c","d").skip(2);
		assertThat(skip.collect(toList()), is(Arrays.asList("c","d")));
	}
	
	@Test
	public void reduce() throws Exception {
		Optional<String> reduce = Stream.of("a","b","c").reduce((e1,e2 )-> e1+e2);
		assertThat(reduce, is(Optional.of("abc")));
	}
	
	@Test
	public void anyMatch() throws Exception {
		boolean anyMatch = Stream.of("a","b","c").anyMatch(e -> e.equals("b"));
		assertThat(anyMatch, is(true));
	}
	
	@Test
	public void allMatch() throws Exception {
		boolean allMatch = Stream.of("aa","ab","ac").anyMatch(e -> e.startsWith("a"));
		assertThat(allMatch, is(true));
	}
	
	@Test
	public void toMap() throws Exception {
		Map<Integer,Employee> map = new HashMap<>();
		map.put(1, new Employee(1,23,"emp1"));
		map.put(2, new Employee(2,24,"emp2"));
		map.put(3, new Employee(3,25,"emp3"));
		
		Stream<Employee> stream = Stream.of(new Employee(1, 23, "emp1"),new Employee(2, 24, "emp2"),new Employee(3, 25, "emp3"));
		Map<Integer, Employee> collect = stream.collect(Collectors.toMap(e -> e.getId(),e -> e));
		assertThat(collect, is(map));
	}
	
	@Test
	public void groupingBy_() throws Exception {
		Map<Integer,List<Employee>> map = new HashMap<>();
		map.put(23, Arrays.asList(new Employee(1,23,"emp1"),new Employee(4,23,"emp4")));
		map.put(24, Arrays.asList(new Employee(2,24,"emp2")));
		map.put(25, Arrays.asList(new Employee(3,25,"emp3")));
		
		Stream<Employee> stream = Stream.of(new Employee(1, 23, "emp1"),new Employee(2, 24, "emp2"),new Employee(3, 25, "emp3"),new Employee(4, 23, "emp4"));
		Map<Integer, List<Employee>> collect = stream.collect(groupingBy(e -> e.getAge()));
		assertThat(collect, is(map));
	}
	
	@Test
	public void partitioningBy_() throws Exception {
		Map<Boolean,List<Employee>> map = new HashMap<>();
		map.put(true, Arrays.asList(new Employee(1,23,"emp1"),new Employee(3,25,"emp3")));
		map.put(false, Arrays.asList(new Employee(2,24,"emp2")));
		
		Stream<Employee> stream = Stream.of(new Employee(1, 23, "emp1"),new Employee(2, 24, "emp2"),new Employee(3, 25, "emp3"));
		Map<Boolean, List<Employee>> collect = stream.collect(partitioningBy(e -> e.getAge() % 2 != 0));
		assertThat(collect, is(map));
	}
	
	
	private class Employee {
		private int id;
		private int age;
		private String name;
		
		public Employee(int id, int age, String name) {
			super();
			this.id = id;
			this.age = age;
			this.name = name;
		}
		
		public int getId() {
			return id;
		}

		public int getAge() {
			return age;
		}

		public String getName() {
			return name;
		}



		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + id;
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Employee other = (Employee) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (id != other.id)
				return false;
			return true;
		}
		private Sample getOuterType() {
			return Sample.this;
		}
		
		
	}
}
