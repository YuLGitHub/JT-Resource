package math;

import org.junit.Test;

public class Exam {
	
	@Test
	public void test(){
		char ascii = 98;
		System.out.println(ascii);
		double width = 6.0;
		double leg = 4.9;
		System.out.println(width - leg);
		int a = 3;
		int b = 5;
		System.out.println(a&b);
		System.out.println(a|b);
		int c = b = 9;
		System.out.println(c + "," + b);
		int d = Integer.MIN_VALUE;
		System.out.println(d<<10);
	}
	
}
