package it.kasoale.utils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by kasoale on 06/11/2016.
 */
public class Test {

    public void testJS(String[] args){
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");

        try {
            engine.eval(Files.newBufferedReader(Paths.get(ConnectionProperties.getValue("base64_decode")), StandardCharsets.UTF_8));

            Invocable invocable = (Invocable) engine;
            //Object obj = invocable.invokeFunction("base64_decode", "aHR0cDovLzE2My4xNzIuMjA5LjE5Ojg3ODIvdifhahdeeaf3RsNmRsMmx0ZHh5ZnR4eHlvZ25hZHlvaXNpeXBidXo0djRianB2ejN2ZWp5d2d1c2RsbzZjdTZ6ZnpxL3YubXA0", "ajchgjfifajf+9pQwR7GHIQ==");
            Object obj = invocable.invokeFunction("base64_decode", "aHR0cDovLzE2My4xNzIuNDAuMzM6hcfehfbahjODc4MC94M2w2eWx0bXQzeHlmdHh4eXBjNWFlYWFjZWNtZHpjYjN6ZjJ1dDZ2Y2V5aGJnZWNicWR0emo2dnJrY2Evdi5tcDQ", 28);

            System.out.println(obj.toString());

        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
