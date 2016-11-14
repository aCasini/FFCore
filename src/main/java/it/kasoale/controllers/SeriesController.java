package it.kasoale.controllers;

import it.kasoale.beans.SerieTV;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Produces;
import java.util.List;

/**
 * Created by kasoale on 13/11/2016.
 */
@RestController
public class SeriesController {

    private static Logger logger = Logger.getLogger(SeriesController.class);

    @Produces("application/json")
    @RequestMapping(value = "/serie", method = RequestMethod.GET)
    public SerieTV getSerieTV(@RequestParam(value = "serieName", defaultValue = "") String serieName){

        return null;
    }
}
