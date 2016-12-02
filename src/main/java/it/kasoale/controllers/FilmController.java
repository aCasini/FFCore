package it.kasoale.controllers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.kasoale.beans.Film;

import it.kasoale.beans.FilmDetails;
import it.kasoale.beans.FilmDetailsList;
import it.kasoale.database.dao.impl.JDBCFilmDAO;

import it.kasoale.ff.parsing.EngineWrapper;
import it.kasoale.utils.ApiProperties;
import it.kasoale.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Created by kasoale on 28/10/2016.
 */
@RestController
public class FilmController {

    private static Logger logger = LoggerFactory.getLogger(FilmController.class);

    @Produces("application/json")
    @RequestMapping(value = "/films", method = RequestMethod.GET)
    public List<Film> getFilms(@RequestParam(value = "filmName", defaultValue = "world") String filmName){

        JDBCFilmDAO filmDAO = JDBCFilmDAO.getInstanceDao();
        List<Film> filmList = filmDAO.getFilmsByFilmName(filmName);

        if(filmList.isEmpty()){
            logger.info("Start SearchEngine for Streaming Information");
            filmList = EngineWrapper.searchStreamingInfo(filmName, false);
        }else{
            logger.info("Start SearchEngine for Streaming Information in order to Update the URL Streaming");
            filmList = EngineWrapper.searchStreamingInfo(filmName, true);
        }

        /*
        for (Film film : filmList) {
            String title = film.getFilmName();
            title = title.replace("(Sub-ITA)","");
            title = title.replace("[HD]","");
            title = title.replaceAll("\\(.*\\)", "");

            logger.info("Get Details for --> "+title);
            FilmDetailsList filmDetailsList = getDetails(title);

            for (FilmDetails filmDetail : filmDetailsList.getFilmsDetails()) {
                if(title.contains(filmDetail.getTitle())){
                    film.setFilmDetails(filmDetail);
                }
            }
        }
        */
        return filmList;


    }

    @Produces("application/json")
    @RequestMapping(value = "/film/detail", method = RequestMethod.GET)
    public FilmDetails getDetail(@RequestParam(value = "filmName", required = true) String filmName){
        logger.info("GET Request films ...");
        FilmDetailsList filmDetailsList = null;

        filmName = filmName.replace(" ","+");
        filmName = filmName.replace("(Sub-ITA)","");
        filmName = filmName.replace("[HD]","");
        filmName = filmName.replaceAll("\\(.*\\)", "");

        String urlString = ApiProperties.getValue("host")+"movie?api_key="+ApiProperties.getValue("api.key")+"&query="+filmName+ApiProperties.getValue("language");
        logger.info(urlString);

        try{
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            String jsonList = Utils.getStringFromInputStream(conn.getInputStream());
            conn.disconnect();


            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);

            logger.info("JSON: "+jsonList);
            filmDetailsList = objectMapper.readValue(jsonList, FilmDetailsList.class);

        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return filmDetailsList.getFilmsDetails().get(0);
    }

    @Produces("application/json")
    @RequestMapping(value = "/film/details", method = RequestMethod.GET)
    public FilmDetailsList getDetails(@RequestParam(value = "filmName", required = true) String filmName){
        logger.info("GET Request films ...");
        FilmDetailsList filmDetailsList = null;

        filmName = filmName.replace(" ","+");
        filmName = filmName.replace("(Sub-ITA)","");
        filmName = filmName.replace("[HD]","");
        filmName = filmName.replaceAll("\\(.*\\)", "");

        String urlString = ApiProperties.getValue("host")+"movie?api_key="+ApiProperties.getValue("api.key")+"&query="+filmName+ApiProperties.getValue("language");
        logger.info(urlString);

        try{
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            String jsonList = Utils.getStringFromInputStream(conn.getInputStream());
            conn.disconnect();


            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);

            logger.info("JSON: "+jsonList);
            filmDetailsList = objectMapper.readValue(jsonList, FilmDetailsList.class);

        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return filmDetailsList;
    }

    @Produces("application/json")
    @RequestMapping(value = "/film", method = RequestMethod.GET)
    public Film getfilm(@RequestParam(value = "filmName", required = true) String filmName){
        logger.info("GET Request film ...");

        JDBCFilmDAO filmDAO = JDBCFilmDAO.getInstanceDao();
        Film film = filmDAO.getFilmByFilmName(filmName);

        if(film == null){
            logger.info("Film not found");
            logger.info("Start SearchEngine for Streaming Information");
            if(EngineWrapper.searchStreamingInfo(filmName, false) != null) {
                if(EngineWrapper.searchStreamingInfo(filmName, true).size() > 0) {
                    film = EngineWrapper.searchStreamingInfo(filmName, true).get(0);
                }
            }
        }else{
            logger.info("Film already present -> don't start the parting");
        }


        return film;
    }

    @RequestMapping(value = "/film", method = RequestMethod.POST)
    public String postfilm(@RequestBody Film film){
        logger.info("POST Request ... ");
        logger.info(film.toString());

        if(film.isValid()){
            JDBCFilmDAO filmDAO = JDBCFilmDAO.getInstanceDao();
            filmDAO.insertFilm(film);

            return "202";
        }else {
            logger.error("BAD Request");
            return "400";
        }

    }
}
