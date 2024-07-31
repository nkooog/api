package test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Date;

@SpringBootTest
class Demo2ApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void test() {
		Date now = new Date();
		Date accessValidity = new Date(now.getTime());

		System.out.println(accessValidity);
		System.out.println(LocalDateTime.now().format());
	}

}
