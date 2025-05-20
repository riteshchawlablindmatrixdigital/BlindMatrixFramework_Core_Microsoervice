package blind.matrix.systems.core.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class BlindMatrixFrameworkCoreSystemsAppnApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlindMatrixFrameworkCoreSystemsAppnApplication.class, args);
    }

}
