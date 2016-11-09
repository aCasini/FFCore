package it.kasoale.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by kasoale on 01/11/2016.
 */
public class ParsingProperties {

    private static Properties parsing = new Properties();
    private static InputStream inputStream = null;

    private static Logger logger = LoggerFactory.getLogger(ParsingProperties.class);

    public void init(){
        try{
            logger.info("Loading the Parsing properties");
            //inputStream = new FileInputStream("./src/main/resources/parsing.properties");
            inputStream = this.getClass().getResourceAsStream("/parsing.properties");
            parsing.load(inputStream);
            logger.info("Properties Loaded");
        }catch (IOException e){
            logger.error(e.getMessage());
        }
    }

    public static String getValue(String key){
        return  parsing.getProperty(key);
    }

}
