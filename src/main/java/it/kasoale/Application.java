package it.kasoale;

import it.kasoale.utils.ConnectionProperties;
import it.kasoale.utils.ParsingProperties;
import it.kasoale.utils.StatementsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by kasoale on 28/10/2016.
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        ConnectionProperties connectionProperties = new ConnectionProperties();
        connectionProperties.init();

        StatementsProperties statementsProperties = new StatementsProperties();
        statementsProperties.init();

        ParsingProperties parsingProperties = new ParsingProperties();
        parsingProperties.init();
    }
}
