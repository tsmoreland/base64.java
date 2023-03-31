package moreland.base64.cli;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration
class ApplicationTests {

	@Test
	void contextLoads() {
		// this test is more a sanity check that we get this far rather than the below assert
		assertDoesNotThrow(() -> {});
	}

}
