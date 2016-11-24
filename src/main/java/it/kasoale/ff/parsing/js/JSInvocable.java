package it.kasoale.ff.parsing.js;

import it.kasoale.utils.ConnectionProperties;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * Created by kasoale on 07/11/2016.
 */
public class JSInvocable {

    private static Logger logger = Logger.getLogger("JSInvocable");
    private final String FUNCTION_NAME = "base64_decode";

    //var linkfile =".*" --> recuper del encodedURL
    //var .* = [0-9][0-9]; --> recupero della key


    public static String decriptURLVideo(String filmUrlEncoded, int key){
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");

        //logger.info("Film Url Encoded: "+filmUrlEncoded);
        //logger.info("Key: "+key);

        try {

            engine.eval(Files.newBufferedReader(Paths.get(ConnectionProperties.getValue("base64_decode")), StandardCharsets.UTF_8));

            Invocable invocable = (Invocable) engine;
            Object filmDecoded = invocable.invokeFunction("base64_decode", filmUrlEncoded, key);
            //Object obj = invocable.invokeFunction("base64_decode", "aHR0cDovLzE2My4xNzIhdbfbhigeeuMjA5LjE5Ojg3ODcvcnhsNml0amRzaHh5ZnR4eHltY25hZ2lmanI1dHlnZHF0dnc2ZGZyNzd4ZHR1b2s3MmJnZTNod2lxZnlxL3YuZmx2", 19);

            //logger.info("URL Decoded: "+filmDecoded.toString());
            System.out.println(filmDecoded.toString());

            return filmDecoded.toString();

        } catch (ScriptException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

}
