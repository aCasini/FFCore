package it.kasoale.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by acasini on 02/12/16.
 */
public class ApiProperties {

    private static Properties api = new Properties();
    private static InputStream inputStream = null;

    private static Logger logger = LoggerFactory.getLogger(ApiProperties.class);

    public void init(){
        try{
            logger.info("Loading the Connection properties");
            inputStream = this.getClass().getResourceAsStream("/moviedb.properties");
            api.load(inputStream);
            logger.info("Properties Loaded");
        }catch (IOException e){
            logger.error(e.getMessage());
        }
    }

    public static String getValue(String key){
        return  api.getProperty(key);
    }
}
