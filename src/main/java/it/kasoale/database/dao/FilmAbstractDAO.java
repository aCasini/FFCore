package it.kasoale.database.dao;

import it.kasoale.utils.ConnectionProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by kasoale on 30/10/2016.
 */
public abstract class FilmAbstractDAO implements FilmDAO{

    private Logger logger = LoggerFactory.getLogger(FilmAbstractDAO.class);
    private Connection connection = null;

    public Connection getConnection(){
        logger.info("Retrieval Connection ... ");
        if(connection == null){
            logger.info("Create a new Connection to "+ ConnectionProperties.getValue("jdbc.host"));
            String host = ConnectionProperties.getValue("jdbc.host.mysql");
            String port = ConnectionProperties.getValue("jdbc.port.mysql");
            String user = ConnectionProperties.getValue("jdbc.user.mysql");
            String pass = ConnectionProperties.getValue("jdbc.pass.mysql");
            String dbNama = ConnectionProperties.getValue("jdbc.database.mysql");

            try {
                Class.forName("com.mysql.jdbc.Driver");
                logger.info("Driver Loaded");
                connection = DriverManager.getConnection("jdbc:mysql://"+host + ":" + port, user, pass);
            }catch (Exception e){
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }

        return connection;

    }

    public boolean closeConnection(){
        logger.info("Closing Connection ...");
        if(connection != null){
            try {
                connection.close();
                logger.info("Connection Closed");
                return true;
            }catch (Exception e){
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
        return false;
    }

}
