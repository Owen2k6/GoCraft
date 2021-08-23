// 
// Decompiled by Procyon v0.5.36
// 

package patches.com.johnymuffin.evolutions.beta;

import com.johnymuffin.evolutions.beta.simplejson.parser.ParseException;
import java.io.InputStream;
import com.johnymuffin.evolutions.beta.simplejson.parser.JSONParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.net.URL;
import com.johnymuffin.evolutions.beta.simplejson.JSONObject;
import java.io.IOException;
import java.io.Reader;

public class JsonReader
{
    private static String readAll(final Reader rd) throws IOException {
        final StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char)cp);
        }
        return sb.toString();
    }
    
    public static JSONObject readJsonFromUrl(final String url) throws IOException, ParseException {
        final InputStream is = new URL(url).openStream();
        try {
            final BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            final String jsonText = readAll(rd);
            final JSONParser parser = new JSONParser();
            final JSONObject json = (JSONObject)parser.parse(jsonText);
            return json;
        }
        finally {
            is.close();
        }
    }
}
