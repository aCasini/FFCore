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
public class ConnectionProperties {

    private static Properties connection = new Properties();
    private static InputStream inputStream = null;

    private static Logger logger = LoggerFactory.getLogger(ConnectionProperties.class);


    public void init(){
        try{
            logger.info("Loading the Connection properties");
            //inputStream = new FileInputStream("./src/main/resources/connection.properties");
            inputStream = this.getClass().getResourceAsStream("/connection.properties");
            connection.load(inputStream);
            logger.info("Properties Loaded");
        }catch (IOException e){
            logger.error(e.getMessage());
        }
    }

    public static String getValue(String key){
        return  connection.getProperty(key);
    }

}
