package com.example.conference;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ConferenceApplicationTests {

  @Test
  void contextLoads() {
    // Verifies the full Spring context boots against H2: all beans
    // (config, repositories, services, mappers, controllers, security) wire up.
  }
}
