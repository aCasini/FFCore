package it.kasoale.database.dao.impl;

import it.kasoale.beans.Episode;
import it.kasoale.beans.Season;
import it.kasoale.beans.SerieTV;
import it.kasoale.database.dao.FilmAbstractDAO;
import it.kasoale.database.dao.ManagerDAO;
import it.kasoale.database.dao.SerieDAO;
import it.kasoale.utils.ConnectionProperties;
import org.apache.log4j.Logger;

import javax.sql.ConnectionPoolDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by kasoale on 20/11/2016.
 */
public class JDBCSerieDAO implements SerieDAO{

    private static Logger logger = Logger.getLogger(JDBCSerieDAO.class);

    @Override
    public void insertEpisode(Episode episode) {
        String INSERT_EPISODE = ConnectionProperties.getValue("insert.episode");

        try {
            Connection connection = ManagerDAO.getInstance().getConnectionPool().getConnection();
            if (connection != null) {
                //TODO: impl me
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void insertSeason(Season season) {
        String INSERT_SEASON = ConnectionProperties.getValue("insert.season");

        try {
            Connection connection = ManagerDAO.getInstance().getConnectionPool().getConnection();
            if (connection != null) {
                //TODO: impl me
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void insertSerie(SerieTV serieTV) {
        String INSERT_SERIE = ConnectionProperties.getValue("insert.serie");
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
