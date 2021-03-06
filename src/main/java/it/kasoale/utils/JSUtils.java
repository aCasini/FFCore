package it.kasoale.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kasoale on 23/11/2016.
 */
public class JSUtils {

    public static int retrievalKey(String inputHTML){
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

    public static String retrievalEncodedUrl(String inputHTML){
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
