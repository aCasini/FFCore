package it.kasoale.ff.parsing.impl;

import it.kasoale.beans.Episode;
import it.kasoale.beans.Season;
import it.kasoale.beans.SerieTV;
import it.kasoale.ff.parsing.IEngine;
import it.kasoale.ff.parsing.js.JSInvocable;
import it.kasoale.utils.ParsingProperties;
import org.apache.log4j.Logger;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kasoale on 13/11/2016.
 */
public class EngineSerie extends AbstractEngine<SerieTV> {

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
                    //logger.info("Serie TV URL: "+serieTV_URL);
                    Elements locandinaElements = element.select("img");
                    for (Element locandina : locandinaElements) {
                        String locandinaURL = locandina.attr("src");
                        //logger.info("Locandina URL: "+locandinaURL);
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

            //logger.info("SERIE TV: \n"+serieTV.toString());


            //stretchMe
            Elements imageSeason = docSeries.select("div");
            String seasonImageURL = null;
            for (Element elSeason : imageSeason) {

                if(elSeason.attr("class").equals("stretchMe parallax")
                        && elSeason.attr("data-stretch") != null) {
                    //logger.info("Season Image: "+elSeason.attr("data-stretch"));
                    seasonImageURL = elSeason.attr("data-stretch");
                }
            }

            //Determina il numero di Stagioni
            Elements numSeasonsEle = docSeries.select("option");
            int seasonNumenr = numSeasonsEle.size();
            logger.info("looking for the seasons numbers ... ");
            for (Element elSeason : numSeasonsEle) {
                logger.info("Add the new Season to Seaaon List");
                Season season = new Season();
                season.setImageSeason(seasonImageURL);
                season.setSeasonID(elSeason.html());
                seasons.add(season);
            }

            for (Season season : seasons) {
                Elements seasonsEleName = docSeries.select("h1");
                for (Element elSeason : seasonsEleName) {
                    if(elSeason.attr("class").equals("title-bg-wrap")){
                        season.setName(elSeason.html());
                    }
                }
            }



            ArrayList<String> titleList = new ArrayList<>();
            ArrayList<String> descriptionList = new ArrayList<>();
            ArrayList<String> episodeURLEncodedlist = new ArrayList<>();
            ArrayList<String> episodeIDList = new ArrayList<>();
            ArrayList<String> imageUrlList = new ArrayList<>();

            //recupero le informazioni relative gli episodi della stagione
            for (Season season: seasons) {


                String seasonNumber = season.getSeasonID().split("Stagione ")[1].trim();
                logger.info("*** Season - " + seasonNumber + " - ***");


                Elements spanEpisodeElements = docSeries.select("span");

                for (Element elementEpisode : spanEpisodeElements) {
                    if(elementEpisode.attr("meta-stag") != null
                        && elementEpisode.attr("meta-stag").equals(seasonNumber)) {
                        String episodeURLEncoded = elementEpisode.attr("meta-embed");
                        String episodeNumber = elementEpisode.attr("meta-ep");
                        //logger.info("Episode " + episodeNumber + " -- " + episodeURLEncoded);

                        episodeIDList.add("S"+seasonNumber+"x"+episodeNumber);

                        //***** decode the URL

                        Document tempDoc = Jsoup.connect(episodeURLEncoded).userAgent(USER_AGENT).referrer(REFERER_URL).get();
                        int key = retrievalKey(tempDoc.html());
                        String encodedUrl = retrievalEncodedUrl(tempDoc.html());

                        if(encodedUrl != null){
                            episodeURLEncodedlist.add(JSInvocable.decriptURLVideo(encodedUrl, key));
                        }
                        //*****

                        //episodeURLEncodedlist.add(episodeURLEncoded);


                        Elements imgEpisodeElements = docSeries.select("img");
                        episodesLoop:
                        for (Element elementImg : imgEpisodeElements) {
                            if (elementImg.attr("meta-src") != null
                                    && elementImg.attr("meta-src").contains("-" + seasonNumber + "x" + episodeNumber + "-")) {
                                String imageElemntUrl = elementImg.attr("meta-src");
                                //logger.info("img episode: " + imageElemntUrl);
                                imageUrlList.add(imageElemntUrl);
                                break episodesLoop;
                            }
                            if (seasonNumber.equals("5")) {
                                if (elementImg.attr("data-original") != null
                                        && elementImg.attr("data-original").contains("-" + seasonNumber + "x" + episodeNumber + "-")) {
                                    String imageElemntUrl = elementImg.attr("data-original");
                                    //logger.info("img episode: " + imageElemntUrl);
                                    imageUrlList.add(imageElemntUrl);
                                    break episodesLoop;
                                }
                            }

                        }
                    }

                    if(elementEpisode.attr("class=") != null
                            && elementEpisode.attr("class").equals("pull-left bottom-year") ){
                        String episodeTitle = elementEpisode.html();

                        if(!titleList.contains(episodeTitle)){
                            //logger.info("Title: "+episodeTitle);
                            titleList.add(episodeTitle);
                        }
                    }

                }

                //Recupera la descrizione dell episodio
                Elements spanEpisodeElementsDesc = docSeries.select("p");
                for (Element elemepDesc : spanEpisodeElementsDesc) {
                    if(elemepDesc.attr("meta-desc") != null
                            && elemepDesc.attr("meta-desc").length() > 40){
                        if(!descriptionList.contains(elemepDesc.attr("meta-desc"))){
                            String episodeDesc = elemepDesc.attr("meta-desc");
                            logger.info(" desc: "+ episodeDesc);

                            descriptionList.add(episodeDesc);
                        }

                    }
                }

            }

            int index = 0;
            logger.info("Size episodeIDList --> "+episodeIDList.size());
            logger.info("Size titleList --> "+titleList.size());
            logger.info("Size descriptionList --> "+descriptionList.size());
            logger.info("Size imageUrlList --> "+imageUrlList.size());
            logger.info("Size episodeURLEncodedlist --> "+ episodeURLEncodedlist.size());
            for (Season season : seasons) {
                String seasonNumber = "S"+season.getSeasonID().split("Stagione ")[1].trim()+"x";

                List<Episode> episodeList = new ArrayList<Episode>();
                for (String episodeID :episodeIDList) {
                    //logger.info("episodeID --> "+episodeID);
                    //logger.info("seasonNumber --> "+seasonNumber);
                    if(episodeID.contains(seasonNumber)){
                        //logger.info("am in season ... "+seasonNumber);
                        //for (int i = 0; i < episodeIDList.size()-1 ; i++) {
                        /**
                            if(index == episodeIDList.size()
                                    || index == episodeURLEncodedlist.size()
                                    || index == descriptionList.size()){
                                break;
                            }
                        **/
                            Episode episode = new Episode();
                            if(index < titleList.size()) {
                                episode.setTitle(titleList.get(index));
                            }

                            if(index < descriptionList.size()) {
                                episode.setDescription(descriptionList.get(index));
                            }

                            if(index < episodeIDList.size()) {
                                episode.setEpisodeID(episodeIDList.get(index));
                            }

                            if(index < imageUrlList.size()) {
                                episode.setImageEpisode(imageUrlList.get(index));
                            }

                            if(index < episodeURLEncodedlist.size()) {
                                episode.setStreamingURL(episodeURLEncodedlist.get(index));
                            }

                            index++;
/**
                            logger.info("\n*******************************");
                            logger.info("\n"+episode.toString());
                            logger.info("\n*******************************\n");
**/
                            episodeList.add(episode);
                        //}
                    }
                }
                season.setEpisodes(episodeList);

                logger.info("SEASON: "+season.toString());
            }

            serieTV.setSeasons(seasons);


        }catch (HttpStatusException ex){
            logger.error("Status CODE: "+ex.getStatusCode());
            ex.printStackTrace();
            return null;
        } catch (Exception ex){
            ex.printStackTrace();
            logger.error(ex.getMessage());
            return null;
        }
//****************

//*************
        return serieTV;
    }


    /**
    public static void main(String[] args){
        ParsingProperties p = new ParsingProperties();
        p.init();

        EngineSerie engineSerie = new EngineSerie();
        engineSerie.parseHTML("fringe", true);

//        engineSerie.parseHTML("dexter", true);

    }
     **/
}
