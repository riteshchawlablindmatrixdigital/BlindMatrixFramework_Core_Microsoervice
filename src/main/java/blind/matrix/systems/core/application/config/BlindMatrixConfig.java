package blind.matrix.systems.core.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

@Configuration
public class BlindMatrixConfig {

    //change this below URL as per your snowflake instance
    private static final String jdbcUrl = "jdbc:snowflake://ro54113.ap-south-1.aws.snowflakecomputing.com/";

    //change this select statement, but make sure the logic below is hard coded for now.
    public static final String selectSQL = "SELECT * FROM  SNOWFLAKE_SAMPLE_DATA.TPCH_SF1.CUSTOMER LIMIT 60";

    /*@Bean
    public DataSource jdbcSqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/springjdbc");
        dataSource.setUsername("RITESHRAJCHAWLA");
        dataSource.setPassword("1001.117777**Gbu");
        dataSource.setUrl(jdbcUrl);
        return dataSource;
    }*/

    @Bean
    @Description("Spring Message Resolver")
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return messageSource;
    }

    @Bean
    @Scope("prototype")
    public Connection getDatabaseConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbcUrl, getJDBCConnProps());
            System.out.println("\tConnection established, connection id : " + connection);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return connection;
    }

    private Properties getJDBCConnProps() {
        //properties object
        Properties properties = new Properties();
        //setting properties
        properties.put("user", "RITESHRAJCHAWLA");
        properties.put("password", "1001.117777**Gbu");
        properties.put("account", "ro54113.ap-south-1.aws"); //account-id followed by cloud region.
        properties.put("warehouse", "SNOWFLAKE_LEARNING_WH");
        properties.put("db", "SNOWFLAKE_SAMPLE_DATA");
        properties.put("schema", "TPCH_SF1");
        properties.put("role", "ACCOUNTADMIN");
        return properties;
    }


}
