package it.kasoale.database.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import it.kasoale.utils.ConnectionProperties;
import org.apache.log4j.Logger;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by kasoale on 20/11/2016.
 */
public class ManagerDAO {

    private static Logger logger = Logger.getLogger(ManagerDAO.class);
    private Connection connection = null;
    private ComboPooledDataSource connectionPool = null;

    private static ManagerDAO instance = null;

    private ManagerDAO(){
        super();
    }

    public static ManagerDAO getInstance(){
        if(instance == null){
            instance = new ManagerDAO();
        }

        return instance;
    }

    public ComboPooledDataSource getConnectionPool(){
        logger.info("Retrieval a ComboPool Connection ... ");
        if(connectionPool == null ){
            connectionPool = new ComboPooledDataSource();

            try {
                String host = ConnectionProperties.getValue("jdbc.host.mysql");
                String port = ConnectionProperties.getValue("jdbc.port.mysql");
                String user = ConnectionProperties.getValue("jdbc.user.mysql");
                String pass = ConnectionProperties.getValue("jdbc.pass.mysql");

                connectionPool.setDriverClass("com.mysql.jdbc.Driver");
                connectionPool.setJdbcUrl("jdbc:mysql://"+host + ":" + port);
                connectionPool.setUser(user);
                connectionPool.setPassword(pass);

                // the settings below are optional -- c3p0 can work with defaults
                connectionPool.setMinPoolSize(5);
                connectionPool.setAcquireIncrement(5);
                connectionPool.setMaxPoolSize(20);
            } catch (PropertyVetoException e) {
                e.printStackTrace();
                connectionPool.close();
                connectionPool = null;
            }
        }

        return connectionPool;

    }

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
