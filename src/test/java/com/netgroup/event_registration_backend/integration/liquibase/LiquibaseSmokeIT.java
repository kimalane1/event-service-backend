package com.netgroup.event_registration_backend.integration.liquibase;


import static org.junit.jupiter.api.Assertions.assertTrue;
import com.netgroup.event_registration_backend.integration.BaseIntegrationTest;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LiquibaseSmokeIT extends BaseIntegrationTest {

  @Autowired
  DataSource dataSource;

  @Test
  void liquibaseShouldCreatePersonTable() throws Exception {
    try (var conn = dataSource.getConnection();
        var statement = conn.createStatement();
        var resultSet = statement.executeQuery(
            "SELECT table_name FROM information_schema.tables WHERE table_name = 'person'")
    ) {
      assertTrue(resultSet.next(), "Table exists");
    }
  }
}
