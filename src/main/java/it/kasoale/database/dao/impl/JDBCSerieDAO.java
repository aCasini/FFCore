package it.kasoale.database.dao.impl;

import it.kasoale.beans.Episode;
import it.kasoale.beans.Season;
import it.kasoale.beans.SerieTV;
import it.kasoale.database.dao.FilmAbstractDAO;
import it.kasoale.database.dao.ManagerDAO;
import it.kasoale.database.dao.SerieDAO;
import it.kasoale.utils.StatementsProperties;
import org.apache.log4j.Logger;

import javax.sql.ConnectionPoolDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by kasoale on 20/11/2016.
 */
public class JDBCSerieDAO implements SerieDAO{

    private static Logger logger = Logger.getLogger(JDBCSerieDAO.class);

    private static JDBCSerieDAO instance = null;

    private JDBCSerieDAO(){
        super();
    }

    public static JDBCSerieDAO getInstance(){
        if(instance == null){
            instance = new JDBCSerieDAO();
        }

        return instance;
    }

    @Override
    public void insertEpisode(Season season) {
        String seasonID = season.getSeasonID();
        String INSERT_EPISODE = StatementsProperties.getValue("insert.episode");

        Connection connection = null;
        try {
            connection = ManagerDAO.getInstance().getConnectionPool().getConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(INSERT_EPISODE);

            for (Episode episode : season.getEpisodes()) {
                if (connection != null
                        && episode.getTitle() != null) {
                    //TODO: impl me
                    statement.setString(1, episode.getTitle());
                    statement.setString(2, episode.getEpisodeID());
                    statement.setString(3, episode.getImageEpisode());
                    statement.setString(4, episode.getStreamingURL());
                    statement.setString(5, episode.getDescription());
                    statement.setString(6, seasonID);
                    statement.setString(7, episode.getPreStreamingURL());
                    statement.addBatch();

                }
            }
            statement.executeBatch();
            connection.commit();


        }catch (Exception e){
            e.printStackTrace();
            logger.error("ERROR during the insert on EPISODE table");
        }finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void insertSeason(Season season, String serieTitle) {
        String INSERT_SEASON = StatementsProperties.getValue("insert.season");

        try {
            Connection connection = ManagerDAO.getInstance().getConnectionPool().getConnection();
            if (connection != null) {
                PreparedStatement statement = connection.prepareStatement(INSERT_SEASON);
                statement.setString(1, season.getSeasonID());
                statement.setString(2, season.getName());
                statement.setString(3, season.getImageSeason());
                statement.setString(4, serieTitle);

                int result = statement.executeUpdate();

                if(result == 1){
                    logger.info("Insert completed - Season - "+ season.getSeasonID());
                }
            }
        }catch (Exception e){
            logger.error("ERROR during the insert on SEASON tables");
            e.printStackTrace();
        }

    }

    @Override
    public void insertSerie(SerieTV serieTV) {
        String INSERT_SERIE = StatementsProperties.getValue("insert.serie");
        try {
            Connection connection = ManagerDAO.getInstance().getConnectionPool().getConnection();
            if (connection != null) {

                PreparedStatement statement = connection.prepareStatement(INSERT_SERIE);
                statement.setString(1, serieTV.getTitoloOriginale());
                statement.setString(2, serieTV.getGenere());
                statement.setString(3, serieTV.getNazione());
                statement.setString(4, serieTV.getIdeatore());
                statement.setString(5, serieTV.getProduzione());
                statement.setString(6, serieTV.getAnno());
                statement.setString(7, serieTV.getCast());

                int result = statement.executeUpdate();

                if (result == 1) {
                    logger.info("Insert completed - SerieTV - " + serieTV.getTitoloOriginale());
                }
            }
        }catch (Exception e){
            logger.error("ERROR during the insert on SERIE table");
            e.printStackTrace();
        }
    }

    @Override
    public void updateEpisodeStreamingURL(Episode episode) {

    }

    @Override
    public List<Episode> getEpisodes(String serieName, String seasonNumber) {
        return null;
    }

    @Override
    public Episode getEpisode(String serieName, String seasonNumber, String episodeNumber) {
        return null;
    }

    @Override
    public Season getSeason(String serieName, String sesonNumber) {
        return null;
    }

    @Override
    public List<Season> getSeasons(String serieName) {
        return null;
    }

    @Override
    public SerieTV getSerieTV(String serieName) {
        return null;
    }

    @Override
    public List<SerieTV> getSerieTVs() {
        return null;
    }
}
