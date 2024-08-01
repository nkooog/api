package test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@SpringBootTest
class Demo2ApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void test() {
		long now = (new Date()).getTime();

		// 2. LocalDateTime -> Date 변환
		Date date = java.sql.Timestamp.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		Date exp = java.sql.Timestamp.valueOf(LocalDateTime.now().plusMinutes(30).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		System.out.println(date);
		System.out.println(exp);

	}

}
