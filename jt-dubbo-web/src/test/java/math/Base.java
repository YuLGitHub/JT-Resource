package math;

import org.junit.Test;

public class Base {
	
	@Test
	public void test01() {
		Long sum = 0L;
		Long num = 9L;
		for (int i = 1; i < 10; i++) {
			num = 10 * num + 9;
			sum += num;
		}
		System.out.println(sum);
	}
	
}
