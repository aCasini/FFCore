package it.kasoale.controllers;

import it.kasoale.beans.Film;

import it.kasoale.database.dao.impl.JDBCFilmDAO;

import it.kasoale.ff.parsing.EngineWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;


/**
 * Created by kasoale on 28/10/2016.
 */
@RestController
public class FilmController {

    private static Logger logger = LoggerFactory.getLogger(FilmController.class);

    @Produces("application/json")
    @RequestMapping(value = "/films", method = RequestMethod.GET)
    public List<Film> getFilms(@RequestParam(value = "filmName", defaultValue = "world") String filmName){
        logger.info("GET Request films ...");

        JDBCFilmDAO filmDAO = JDBCFilmDAO.getInstanceDao();
        List<Film> filmList = filmDAO.getFilmsByFilmName(filmName);

        if(filmList.isEmpty()){
            logger.info("Start SearchEngine for Streaming Information");
            filmList = EngineWrapper.searchStreamingInfo(filmName, false);
        }else{
            logger.info("Start SearchEngine for Streaming Information in order to Update the URL Streaming");
            filmList = EngineWrapper.searchStreamingInfo(filmName, true);
        }

        return filmList;

    }

    @Produces("application/json")
    @RequestMapping(value = "/film", method = RequestMethod.GET)
    public Film getfilm(@RequestParam(value = "filmName", defaultValue = "world") String filmName){
        logger.info("GET Request film ...");

        JDBCFilmDAO filmDAO = JDBCFilmDAO.getInstanceDao();
        Film film = filmDAO.getFilmByFilmName(filmName);

        if(film == null){
            logger.info("Film not found");
            logger.info("Start SearchEngine for Streaming Information");
            if(EngineWrapper.searchStreamingInfo(filmName, false) != null) {
                film = EngineWrapper.searchStreamingInfo(filmName, true).get(0);
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
