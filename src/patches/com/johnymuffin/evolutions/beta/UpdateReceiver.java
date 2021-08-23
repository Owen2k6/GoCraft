// 
// Decompiled by Procyon v0.5.36
// 

package patches.com.johnymuffin.evolutions.beta;

import com.johnymuffin.evolutions.beta.nametag.TagColor;
import java.util.Iterator;
import com.johnymuffin.evolutions.beta.simplejson.JSONObject;
import com.johnymuffin.evolutions.beta.simplejson.parser.ParseException;
import com.johnymuffin.evolutions.beta.simplejson.JSONArray;
import com.johnymuffin.evolutions.beta.simplejson.parser.JSONParser;

public class UpdateReceiver
{
    public static void processUpdates(final String text) {
        try {
            final JSONParser jsonParser = new JSONParser();
            final JSONArray updates = (JSONArray)jsonParser.parse(text);
            processUpdates(updates);
        }
        catch (ParseException e) {
            System.out.println("Received an invalid update array, skipping!");
        }
    }
    
    private static void processUpdates(final JSONArray jsonArray) {
        for (final Object object : jsonArray) {
            final JSONObject update = (JSONObject)object;
            processUpdate(update);
        }
    }
    
    private static void processUpdate(final JSONObject jsonObject) {
        if (!jsonObject.containsKey("protocol")) {
            updateError(jsonObject, "Invalid update, doesn't contain protocol id.");
            return;
        }
        if (!jsonObject.containsKey("updateType")) {
            updateError(jsonObject, "Invalid update, doesn't contain update types");
            return;
        }
        if (String.valueOf(jsonObject.get("updateType")).equals("basicNameTagUpdate")) {
            if (!jsonObject.containsKey("player") || !jsonObject.containsKey("color")) {
                updateError(jsonObject, "Not enough fields are provided.");
                return;
            }
            final String playerName = String.valueOf(jsonObject.get("player"));
            final String rawColor = String.valueOf(jsonObject.get("color"));
            final TagColor tagColor = TagColor.valueOf(rawColor);
            if (tagColor != null) {
                BetaEVO.getInstance().basicNameTagUpdate(tagColor, playerName);
                return;
            }
            updateError(jsonObject, "Unknown color type.");
        }
        else if (String.valueOf(jsonObject.get("updateType")).equals("complexNameTagUpdate")) {
            if (jsonObject.containsKey("player") && jsonObject.containsKey("colorCode") && jsonObject.containsKey("backLight")) {
                final String playerName = String.valueOf(jsonObject.get("player"));
                final int colorCode = Integer.valueOf(String.valueOf(jsonObject.get("colorCode")));
                final boolean backLight = Boolean.getBoolean(String.valueOf(jsonObject.get("backLight")));
                BetaEVO.getInstance().complexNameTagUpdate(colorCode, backLight, playerName);
                return;
            }
            updateError(jsonObject, "Not enough fields are provided.");
        }
        else if (String.valueOf(jsonObject.get("updateType")).equals("mouseEars")) {
            if (jsonObject.containsKey("player") && jsonObject.containsKey("enabled")) {
                final String playerName = String.valueOf(jsonObject.get("player"));
                final boolean enabled = Boolean.valueOf(String.valueOf(jsonObject.get("enabled")));
                BetaEVO.getInstance().setMouseEars(enabled, playerName);
                return;
            }
            updateError(jsonObject, "Not enough fields are provided.");
        }
        else {
            if (!String.valueOf(jsonObject.get("updateType")).equals("fly")) {
                updateError(jsonObject, "Unknown Update Type");
                return;
            }
            if (jsonObject.containsKey("enabled")) {
                final boolean enabled2 = Boolean.valueOf(String.valueOf(jsonObject.get("enabled")));
                BetaEVO.getInstance().setAllowFlying(enabled2);
                return;
            }
            updateError(jsonObject, "Not enough fields are provided.");
        }
    }
    
    private static void updateError(final JSONObject jsonObject, final String errorMessage) {
        System.out.println("An error occurred processing an update: " + errorMessage + ". " + jsonObject.toJSONString());
    }
}
