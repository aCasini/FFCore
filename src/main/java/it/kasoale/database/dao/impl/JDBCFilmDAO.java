package it.kasoale.database.dao.impl;

import it.kasoale.beans.Film;
import it.kasoale.database.dao.FilmAbstractDAO;
import it.kasoale.utils.StatementsProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kasoale on 30/10/2016.
 */
public class JDBCFilmDAO extends FilmAbstractDAO {

    private Logger logger = LoggerFactory.getLogger(JDBCFilmDAO.class);
    private static JDBCFilmDAO instance = null;
    private DataSource dataSource;

    private JDBCFilmDAO(){
        super();
    }

    public static JDBCFilmDAO getInstanceDao(){
        if(instance == null){
            instance = new JDBCFilmDAO();
        }

        return instance;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getConnection() {
        return super.getConnection();
    }

    @Override
    public boolean closeConnection() {
        return super.closeConnection();
    }

    @Override
    public Film getFilmByFilmName(String filmName) {
        String SELECT_FILM = StatementsProperties.getValue("select.film");
        Film film = null;

        Connection connection = super.getConnection();
        if(connection != null){
            try {
                SELECT_FILM = SELECT_FILM.replace("?", filmName);
                logger.info(SELECT_FILM);
                PreparedStatement statement = connection.prepareStatement(SELECT_FILM);

                ResultSet resultSet = statement.executeQuery();

                film = new Film();
                while(resultSet.next()) {
                    film.setFilmName(resultSet.getString("FILM_NAME"));
                    film.setFilmUrl(resultSet.getString("FILM_URL"));
                    film.setImageUrl(resultSet.getString("IMAGE_URL"));
                    film.setStreamingUrl(resultSet.getString("STREAMING_URL"));
                }

                resultSet.close();
                //super.closeConnection();
                if(film.isValid()){
                    return film;
                }else{
                    return null;
                }

            }catch (Exception e){
                logger.error("Error during the getFilmByFilmName select ");
                e.printStackTrace();

                return film;
            }
        }else{
            logger.error("Connection is NULL");
            return null;
        }
    }

    @Override
    public List<Film> getFilmsByFilmName(String filmName) {
        String SELECT_FILMS = StatementsProperties.getValue("select.films");
        List<Film> filmList = new ArrayList<>();

        Connection connection = super.getConnection();
        if(connection != null){
            try{
                SELECT_FILMS = SELECT_FILMS.replace("?", filmName);
                logger.info(SELECT_FILMS);
                PreparedStatement statement = connection.prepareStatement(SELECT_FILMS);

                ResultSet resultSet = statement.executeQuery();

                while(resultSet.next()) {
                    Film film = new Film();
                    film.setFilmName(resultSet.getString("FILM_NAME"));
                    film.setFilmUrl(resultSet.getString("FILM_URL"));
                    film.setImageUrl(resultSet.getString("IMAGE_URL"));
                    film.setStreamingUrl(resultSet.getString("STREAMING_URL"));

                    if(film.isValid()) {
                        filmList.add(film);
                    }

                }
                resultSet.close();
                //super.closeConnection();
            }catch (Exception e){
                logger.error("Error during the getFilmsByFilmName select");
                e.printStackTrace();
                return filmList;
            }

        }else{
            logger.info("No films found");
            return filmList;
        }
        return filmList;
    }

    @Override
    public void insertFilm(Film film) {
        String INSERT_FILM = StatementsProperties.getValue("insert.film");

        Connection connection = super.getConnection();
        if(connection != null){
            try {
                PreparedStatement statement = connection.prepareStatement(INSERT_FILM);
                statement.setString(1, film.getFilmName());
                statement.setString(2, film.getImageUrl());
                statement.setString(3, film.getFilmUrl());
                statement.setString(4, film.getStreamingUrl());

                statement.executeUpdate();

            }catch (Exception e){
                e.printStackTrace();
                logger.error("Error during the insertFilm operation");
            }finally {
                //super.closeConnection();
            }
        }
        //super.closeConnection();
    }

    @Override
    public void updateFilmStreaming(Film film){
        String UPDATE_FILM = StatementsProperties.getValue("update.film");
        UPDATE_FILM = UPDATE_FILM.replace("$NEW_STREAM_URL$", film.getStreamingUrl());
        UPDATE_FILM = UPDATE_FILM.replace("?", film.getFilmName());

        Connection connection = super.getConnection();
        if(connection != null){
            try{

                PreparedStatement statement = connection.prepareStatement(UPDATE_FILM);
                statement.executeUpdate();

            }catch (Exception e){
                e.printStackTrace();
                //super.closeConnection();
                logger.error("Error during the updateFilm operation");
            }
        }
    }
}
