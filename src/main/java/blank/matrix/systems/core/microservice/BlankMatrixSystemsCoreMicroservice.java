package blank.matrix.systems.core.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class BlankMatrixSystemsCoreMicroservice {

    public static void main(String[] args) {
        SpringApplication.run(BlankMatrixSystemsCoreMicroservice.class, args);
    }

}
