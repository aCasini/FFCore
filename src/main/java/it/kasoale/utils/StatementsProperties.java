package it.kasoale.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by kasoale on 30/10/2016.
 */
public class StatementsProperties {

    private static Properties statements = new Properties();
    private static InputStream inputStream = null;

    private static Logger logger = LoggerFactory.getLogger(StatementsProperties.class);

    public void init(){
        try{
            logger.info("Loading the Statements properties");
            //inputStream = new FileInputStream("./src/main/resources/statements.properties");
            inputStream = this.getClass().getResourceAsStream("/statements.properties");
            statements.load(inputStream);
            logger.info("Properties Loaded");
        }catch (IOException e){
            logger.error(e.getMessage());
        }
    }

    public static String getValue(String key){
        return  statements.getProperty(key);
    }
}
