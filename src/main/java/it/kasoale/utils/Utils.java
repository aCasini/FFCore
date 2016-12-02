package it.kasoale.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by acasini on 02/12/16.
 */
public class Utils {

    public static String convertRomeNumber(String romeString){
        if(romeString.contains("2")){
            return romeString.replace("2", "II");
        }

        if(romeString.contains("3")){
            return romeString.replace("3", "III");
        }

        if(romeString.contains("4")){
            return romeString.replace("4", "IV");
        }

        return romeString;
    }

    // convert InputStream to String
    public static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

}
