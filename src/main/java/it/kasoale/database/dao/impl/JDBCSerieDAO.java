package it.kasoale.database.dao.impl;

import it.kasoale.beans.Episode;
import it.kasoale.beans.Season;
import it.kasoale.beans.SerieTV;
import it.kasoale.database.dao.FilmAbstractDAO;
import it.kasoale.database.dao.ManagerDAO;
import it.kasoale.database.dao.SerieDAO;
import it.kasoale.ff.parsing.js.JSInvocable;
import it.kasoale.utils.JSUtils;
import it.kasoale.utils.StatementsProperties;
import org.apache.coyote.http2.HpackEncoder;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.sql.ConnectionPoolDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by kasoale on 20/11/2016.
 */
public class JDBCSerieDAO implements SerieDAO{

    private static Logger logger = Logger.getLogger(JDBCSerieDAO.class);
    private final String USER_AGENT         = "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6";
    private final String REFERER_URL        = "http://www.google.com";

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
    public void insertEpisode(Season season, String serieTitle) {
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
                    statement.setString(1, episode.getTitle());
                    statement.setString(2, episode.getEpisodeID());
                    statement.setString(3, episode.getImageEpisode());
                    statement.setString(4, episode.getStreamingURL());
                    statement.setString(5, episode.getDescription());
                    statement.setString(6, seasonID);
                    statement.setString(7, episode.getPreStreamingURL());
                    statement.setString(8, serieTitle);
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
        Connection connection = null;

        try {
            connection = ManagerDAO.getInstance().getConnectionPool().getConnection();
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
        }finally {
            if(connection != null){
                try{
                    connection.close();
                }catch (SQLException e){
                    logger.error("Unable to close the connection");
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void insertSerie(SerieTV serieTV) {
        String INSERT_SERIE = StatementsProperties.getValue("insert.serie");
        Connection connection = null;

        try {
            connection = ManagerDAO.getInstance().getConnectionPool().getConnection();
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
        }finally {
            if(connection != null){
                try{
                    connection.close();
                }catch (SQLException e){
                    logger.error("Impossible to close the connestion");
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void updateEpisodeStreamingURL(Episode episode) {

    }

    @Override
    public List<Episode> getEpisodes(String serieName, String seasonNumber) {
        logger.info(" *** Start getEpisodes ");
        String SELECT_EPISODES = StatementsProperties.getValue("select.episodes");
        SELECT_EPISODES = SELECT_EPISODES.replace("?1", serieName);
        SELECT_EPISODES = SELECT_EPISODES.replace("?2", seasonNumber);

        logger.info(SELECT_EPISODES);
        Connection connection = null;
        List<Episode> episodeList = new ArrayList<>();

        try{
            connection = ManagerDAO.getInstance().getConnectionPool().getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_EPISODES);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                Episode episode = new Episode();
                episode.setDescription(resultSet.getString("DESCRIPTION"));
                episode.setEpisodeID(resultSet.getString("EPISODE_ID"));
                episode.setImageEpisode(resultSet.getString("IMG_URL"));
                episode.setPreStreamingURL(resultSet.getString("PRESTREAMING_URL"));
                //episode.setStreamingURL(resultSet.getString("STREAMING_URL"));
                episode.setTitle(resultSet.getString("TITLE"));

                Document tempDoc = Jsoup.connect(episode.getPreStreamingURL()).userAgent(USER_AGENT).referrer(REFERER_URL).get();
                int key = JSUtils.retrievalKey(tempDoc.html());
                String encodedUrl = JSUtils.retrievalEncodedUrl(tempDoc.html());

                if(encodedUrl != null){
                    episode.setStreamingURL(JSInvocable.decriptURLVideo(encodedUrl, key));
                }


                episodeList.add(episode);
            }
            resultSet.close();

        }catch (Exception e){
            logger.error("ERROR during the select statement from EPISODE table");
            e.printStackTrace();
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("Unable to close connection");
                e.printStackTrace();
            }
        }
        return episodeList;
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
        logger.info(" *** Start getSeasons ");
        String SELECT_SEASONS = StatementsProperties.getValue("select.seasons");
        SELECT_SEASONS = SELECT_SEASONS.replace("?", serieName);

        logger.info(SELECT_SEASONS);

        Connection connection = null;
        List<Season> seasonList = new ArrayList<>();

        try{
            connection = ManagerDAO.getInstance().getConnectionPool().getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_SEASONS);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                Season season = new Season();
                season.setSeasonID(resultSet.getString("SEASON_ID"));
                season.setName(resultSet.getString("NAME"));
                season.setImageSeason(resultSet.getString("IMG_URL"));

                seasonList.add(season);
            }

            resultSet.close();
        }catch (Exception e){
            logger.error("ERROR during the Select statement on SEASON table");
            e.printStackTrace();
        }finally {
            if(connection != null){
                try{
                    connection.close();
                }catch (SQLException e){
                    logger.error("Unable to clese connection");
                    e.printStackTrace();
                }
            }
        }
        return seasonList;
    }

    @Override
    public SerieTV getSerieTV(String serieName) {
        String SELECT_SERIE = StatementsProperties.getValue("select.serie");
        SELECT_SERIE = SELECT_SERIE.replace("?", serieName);

        Connection connection = null;
        SerieTV serieTV = null;
        try{
            connection = ManagerDAO.getInstance().getConnectionPool().getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_SERIE);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                serieTV = new SerieTV();
                serieTV.setTitoloOriginale(resultSet.getString("TITLE_SERIE"));
                serieTV.setGenere(resultSet.getString("GENERE"));
                serieTV.setNazione(resultSet.getString("NAZIONE"));
                serieTV.setIdeatore(resultSet.getString("IDEATORE"));
                serieTV.setProduzione(resultSet.getString("PRODUZIONE"));
                serieTV.setAnno(resultSet.getString("ANNO"));
                serieTV.setCast(resultSet.getString("CAST"));
            }

            resultSet.close();
        }catch (Exception e){
            logger.error("ERROR durong the SELECT on SERIE table");
            e.printStackTrace();
        }finally {
            if(connection != null){
                try {
                    connection.close();
                }catch (SQLException e){
                    logger.error("Unable to close the connection");
                    e.printStackTrace();
                }
            }
        }

        return serieTV;
    }

    @Override
    public List<SerieTV> getSerieTVs() {
        return null;
    }
}
