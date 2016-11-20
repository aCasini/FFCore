package it.kasoale.ff.parsing;

import it.kasoale.beans.Film;
import it.kasoale.database.dao.impl.JDBCFilmDAO;
import it.kasoale.ff.parsing.js.JSInvocable;
import it.kasoale.utils.ParsingProperties;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kasoale on 01/11/2016.
 */
public class Engine{

    private static Logger logger = LoggerFactory.getLogger(Engine.class);

    private final String USER_AGENT         = "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6";
    private final String REFERER_URL        = "http://www.google.com";

    private String prepareURL(String filmName){
        String HTTP_URL    = ParsingProperties.getValue("source.url");
        String HTTP_QUERY  = ParsingProperties.getValue("query.url");
        String PLACEHOLDER = ParsingProperties.getValue("placeholder");

        return (HTTP_URL+HTTP_QUERY).replace(PLACEHOLDER, filmName);
    }

    public List<Film> parseHTTP(String filmName, boolean isPresent){
        String url = prepareURL(filmName);
        List filmList = new ArrayList<>();

        logger.info("Start to Parsing the URL: "+url);
        try {
            Document doc = Jsoup.connect(url).userAgent(USER_AGENT).referrer(REFERER_URL).get();

            Elements filmsTitleElement = doc.getElementsByClass("title");

            Elements linksElements = doc.select("a");
            int i = 0;
            for (Element element: linksElements) {

                String filmUrl = element.attr("href");
                String imageUrl = element.attr("data-thumbnail");

                if((filmUrl != null && filmUrl != "") && (imageUrl != null && imageUrl != "")){
                    String filmTitle = filmsTitleElement.get(i).html();

                    logger.info("Parsing the URL: "+filmUrl);
                    Document docFrame = Jsoup.connect(filmUrl).userAgent(USER_AGENT).referrer(REFERER_URL).get();

                    Element iframe = docFrame.select("iframe").first();
                    logger.info("=========");
                    if(iframe != null){
                        String streamingUrl = iframe.attr("src");
                        if(streamingUrl.contains("youtube")){
                            addFilmToList(isPresent, filmList, filmUrl, imageUrl, filmTitle, streamingUrl);
                            i++;
                            continue;
                        }
                        logger.info("Streaming Url: "+streamingUrl);
                        logger.info("=========");

                        //*********
                        int responceCode = getStatusCode(streamingUrl);
                        if(responceCode >= 404 || responceCode < 200){
                            logger.info("Responce Code: "+responceCode + " for the URL: "+streamingUrl +  " --> SKIP IT");
                            continue;
                        }
                        Document docStreamingUrl = Jsoup.connect(streamingUrl).userAgent(USER_AGENT).referrer(REFERER_URL).get();

                        //var linkfile =".*" --> recuper del encodedURL
                        //var .* = [0-9][0-9]; --> recupero della key

                        int key = retrievalKey(docStreamingUrl.html());
                        String encodedUrl = retrievalEncodedUrl(docStreamingUrl.html());

                        if(encodedUrl != null){
                            streamingUrl = JSInvocable.decriptURLVideo(encodedUrl, key);
                        }

                        //*********
                        addFilmToList(isPresent, filmList, filmUrl, imageUrl, filmTitle, streamingUrl);
                        i++;
                    }else{
                        logger.error("Impossible to parse the HTML, the iframe tag is NULL");
                        return filmList;
                    }

                }
            }

            return filmList;

        }catch (Exception e){
            e.printStackTrace();
            logger.error("Error occured while parsing the URL: "+url);
            return filmList;
        }
    }

    private void addFilmToList(boolean isPresent, List filmList, String filmUrl, String imageUrl, String filmTitle, String streamingUrl) {
        Film film = new Film();
        film.setFilmName(filmTitle);
        film.setFilmUrl(filmUrl);
        film.setImageUrl(imageUrl);
        film.setStreamingUrl(streamingUrl);

        if(!isPresent){
            storageFilm(film);
        }else{
            updateStoragedFilm(film);
        }

        filmList.add(film);
    }


    private int getStatusCode(String urlHttp) throws IOException {
        URL url = new URL(urlHttp);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        int code = connection.getResponseCode();

        return code;
    }

    private void storageFilm(Film film){
        JDBCFilmDAO dao = JDBCFilmDAO.getInstanceDao();
        dao.insertFilm(film);
    }

    private void updateStoragedFilm(Film film){
        JDBCFilmDAO dao = JDBCFilmDAO.getInstanceDao();
        dao.updateFilmStreaming(film);
    }

    private int retrievalKey(String inputHTML){
        Pattern pattern = Pattern.compile("var .* = [0-9].*;");
        Matcher matcher = pattern.matcher(inputHTML);

        int key = 0;

        while( matcher.find() ) {
            String keyJS = matcher.group();
            key = Integer.parseInt(keyJS.split("=")[1].replace(";", "").trim());
            break;
        }

        return key;
    }

    private String retrievalEncodedUrl(String inputHTML){
        Pattern pattern_02 = Pattern.compile("var linkfile =\".*\"");
        Matcher matcher_02 = pattern_02.matcher(inputHTML);

        String encodedUrl = null;
        while( matcher_02.find() ) {
            String encodedUrlJS = matcher_02.group();
            encodedUrl = encodedUrlJS.split("=")[1].replace("\"", "").trim();

            break;
        }

        return encodedUrl;
    }

}
