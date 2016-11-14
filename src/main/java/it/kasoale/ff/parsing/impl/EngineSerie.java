package it.kasoale.ff.parsing.impl;

import it.kasoale.beans.Episode;
import it.kasoale.beans.Season;
import it.kasoale.beans.SerieTV;
import it.kasoale.ff.parsing.IEngine;
import it.kasoale.utils.ParsingProperties;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kasoale on 13/11/2016.
 */
public class EngineSerie implements IEngine<SerieTV> {

    private static Logger logger = Logger.getLogger(EngineSerie.class);

    private final String USER_AGENT         = "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6";
    private final String REFERER_URL        = "http://www.google.com";


    @Override
    public String prepareURL(String searchValue) {
        logger.info("prepare URL for the parsing serie");
        String HTTP_URL    = ParsingProperties.getValue("source.series.url");
        String HTTP_QUERY  = ParsingProperties.getValue("query.series.url");
        String PLACEHOLDER = ParsingProperties.getValue("placeholder.series");



        return (HTTP_URL+HTTP_QUERY).replace(PLACEHOLDER, searchValue);
    }

    @Override
    public SerieTV parseHTML(String searchValue, boolean isPresent) {
        logger.info("Start the parsing for Serie");

        String seriesUrl = prepareURL(searchValue);
        SerieTV serieTV = new SerieTV();
        List<Season> seasons = new ArrayList<Season>();
        List<Episode> episodes = new ArrayList<Episode>();

        try {
            logger.info("--> "+seriesUrl);
            Document doc = Jsoup.connect(seriesUrl).userAgent(USER_AGENT).referrer(REFERER_URL).get();

            String serieTV_URL = null;
            Elements linksSeriesElements = doc.select("a");
            for (Element element: linksSeriesElements) {
                if(element.attr("href").startsWith("http://www.guardaserie.online/")
                        && element.attr("class").equals("box-link-serie")
                        && !element.attr("href").contains("dmca")){
                    serieTV_URL = element.attr("href");
                    logger.info("Serie TV URL: "+serieTV_URL);
                    Elements locandinaElements = element.select("img");
                    for (Element locandina : locandinaElements) {
                        String locandinaURL = locandina.attr("src");
                        logger.info("Locandina URL: "+locandinaURL);
                        serieTV.setImageURL(locandinaURL);
                    }
                }
            }

            if(serieTV_URL == null){
                logger.error("No Series found");
                return null;
            }

            Document docSeries = Jsoup.connect(serieTV_URL).userAgent(USER_AGENT).referrer(REFERER_URL).get();
            //Inject the SerireTV Bean
            Elements elements = docSeries.select("span");
            for (Element el: elements) {
                if(el.attr("class").equals("desc-single-serie")){
                    Elements elValues = el.select("p");
                    for (Element elValue : elValues) {
                        String serieDetails = elValue.html();
                        if(serieDetails.contains("TITOLO")){
                            serieTV.setTitoloOriginale(serieDetails);
                        }else if(serieDetails.contains("GENERE")){
                            serieTV.setGenere(serieDetails);
                        }else if(serieDetails.contains("NAZIONE")){
                            serieTV.setNazione(serieDetails);
                        }else if(serieDetails.contains("IDEATORE")){
                            serieTV.setIdeatore(serieDetails);
                        }else if(serieDetails.contains("PRODUZIONE")){
                            serieTV.setProduzione(serieDetails);
                        }else if(serieDetails.contains("ANNO")){
                            serieTV.setAnno(serieDetails);
                        }else if(serieDetails.contains("CAST")){
                            serieTV.setCast(serieDetails);
                        }
                    }
                }

            }

            logger.info("SERIE TV: \n"+serieTV.toString());


            //Determina il numero di Stagioni
            Elements numSeasonsEle = docSeries.select("option");
            int seasonNumenr = numSeasonsEle.size();
            logger.info("looking for the seasons numbers ... ");
            for (Element elSeason : numSeasonsEle) {
                logger.info(elSeason.html());
            }



            //stretchMe
            Elements imageSeason = docSeries.select("div");
            for (Element elSeason : imageSeason) {
                if(elSeason.attr("class").equals("stretchMe parallax")
                        && elSeason.attr("data-stretch") != null) {
                    logger.info("Season Image: "+elSeason.attr("data-stretch"));
                }
            }


        }catch (Exception ex){
            ex.printStackTrace();
            logger.error(ex.getMessage());
        }

        return null;
    }

    public static void main(String[] args){
        ParsingProperties p = new ParsingProperties();
        p.init();

        EngineSerie engineSerie = new EngineSerie();
        engineSerie.parseHTML("fringe", true);

//        engineSerie.parseHTML("dexter", true);

    }
}
