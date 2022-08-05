package by.zadziarnouski.testcontainers;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
class CustomerIntegrationTests {

  @Autowired
  private CustomerDao customerDao;

  @Container
  private static MySQLContainer container = (MySQLContainer) new MySQLContainer("mysql:latest")
//      .execInContainer("ls", "-la")
      .withClasspathResourceMapping("application.properties", "/tmp/application.properties", BindMode.READ_ONLY)
      .withFileSystemBind("/home/taras/Downloads", "/tmp/downloads", BindMode.READ_WRITE)
      .withReuse(true);

  @DynamicPropertySource
  public static void overrideProps(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", container::getJdbcUrl);
    registry.add("spring.datasource.username", container::getUsername);
    registry.add("spring.datasource.password", container::getPassword);
  }

  @Test
  void when_using_a_clean_db_this_should_be_empty() {
//    String stdout = container.getLogs(OutputType.STDOUT);
//    container.withLogConsumer(new Slf4jLogConsumer());
//    Integer onYourMachine = container.getMappedPort(3306);

    List<Customer> customers = customerDao.findAll();
    Assertions.assertThat(customers).hasSize(2);
  }
}
